package top.hendrixshen.magiclib.util.collect;

import lombok.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.Serializable;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ValueContainer<T> implements Serializable {
    /**
     * Common instance for {@code empty()}.
     */
    private static final ValueContainer<?> EMPTY = new ValueContainer<>(null, false, null);
    private static final long serialVersionUID = -6457485528417039883L;

    private final T value;
    @Getter
    private final boolean exist;
    @Getter
    @Nullable
    private final Throwable exception;

    public static <T> @NotNull ValueContainer<T> of(T value) {
        return new ValueContainer<>(Objects.requireNonNull(value), true, null);
    }

    public static <T> @NotNull ValueContainer<T> ofNullable(T value) {
        return value == null ? ValueContainer.empty() : new ValueContainer<>(value, true, null);
    }

    public static <T> @NotNull ValueContainer<T> empty() {
        @SuppressWarnings("unchecked")
        ValueContainer<T> t = (ValueContainer<T>) EMPTY;
        return t;
    }

    public static <T> @NotNull ValueContainer<T> exception(Throwable exception) {
        return new ValueContainer<>(null, false, exception);
    }

    public T get() {
        if (!this.exist) {
            throw new RuntimeException("Value not exists");
        }

        return this.value;
    }

    public boolean isException() {
        return this.exception != null;
    }

    public boolean isPresent() {
        return this.value != null;
    }

    public boolean isEmpty() {
        return this.value == null;
    }

    public void ifPresent(Consumer<T> action) {
        if (this.isPresent()) {
            action.accept(this.value);
        }
    }

    public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction) {
        if (this.isPresent()) {
            action.accept(this.value);
        } else {
            emptyAction.run();
        }
    }

    public ValueContainer<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);

        if (isPresent()) {
            return predicate.test(this.value) ? this : ValueContainer.empty();
        }

        return this;
    }

    public <U> ValueContainer<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);

        if (this.isPresent()) {
            return ValueContainer.of(mapper.apply(this.value));
        }

        return ValueContainer.empty();
    }

    public <U> ValueContainer<U> flatMap(Function<? super T, ? extends ValueContainer<? extends U>> mapper) {
        Objects.requireNonNull(mapper);

        if (this.isPresent()) {
            @SuppressWarnings("unchecked")
            ValueContainer<U> r = (ValueContainer<U>) mapper.apply(this.value);
            return Objects.requireNonNull(r);
        }

        return ValueContainer.empty();
    }

    public ValueContainer<T> or(Supplier<? extends ValueContainer<? extends T>> supplier) {
        Objects.requireNonNull(supplier);

        if (this.isPresent()) {
            return this;
        } else {
            @SuppressWarnings("unchecked")
            ValueContainer<T> r = (ValueContainer<T>) supplier.get();
            return Objects.requireNonNull(r);
        }
    }

    public Stream<T> stream() {
        return this.isPresent() ? Stream.of(this.value) : Stream.empty();
    }

    public T orElse(T other) {
        return this.isPresent() ? this.value : other;
    }

    public T orElseGet(Supplier<? extends T> supplier) {
        return this.isPresent() ? this.value : supplier.get();
    }

    public T orElseThrow() {
        if (this.isPresent()) {
            return value;
        }

        throw new NoSuchElementException("No value present");
    }

    public <E extends Throwable> T orElseThrow(Supplier<? extends E> exceptionSupplier) throws E {
        if (this.isPresent()) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }
}
