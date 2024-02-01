package top.hendrixshen.magiclib.util.collect.tuple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class ImmutablePair<L, R> extends Pair<L, R> {
    private static final ImmutablePair<?, ?>[] EMPTY_ARRAY = {};
    @SuppressWarnings("rawtypes")
    private static final ImmutablePair NULL = new ImmutablePair<>(null, null);

    private final L left;
    private final R right;

    @Contract("_, _ -> new")
    public static <L, R> @NotNull Pair<L, R> of(L left, R right) {
        return new ImmutablePair<>(Objects.requireNonNull(left, "left"),
                Objects.requireNonNull(right, "right"));
    }

    @Contract("_ -> new")
    public static <L, R> @NotNull Pair<L, R> of(Map.Entry<L, R> pair) {
        return pair == null ? ImmutablePair.nullPair() : new ImmutablePair<>(pair.getKey(), pair.getValue());
    }

    @Contract("_, _ -> new")
    public static <L, R> @NotNull Pair<L, R> ofNullable(L left, R right) {
        return new ImmutablePair<>(left, right);
    }

    @Contract("_ -> new")
    public static <L, R> @NotNull Pair<L, R> left(L left) {
        return ImmutablePair.ofNullable(left, null);
    }

    public static <L, R> @NotNull Pair<L, R> right(R right) {
        return ImmutablePair.ofNullable(null, right);
    }

    @SuppressWarnings("unchecked")
    public static <L, R> ImmutablePair<L, R>[] emptyArray() {
        return (ImmutablePair<L, R>[]) ImmutablePair.EMPTY_ARRAY;
    }

    @SuppressWarnings("unchecked")
    public static <L, R> ImmutablePair<L, R> nullPair() {
        return ImmutablePair.NULL;
    }

    public ImmutablePair(L left, R right) {
        super();
        this.left = left;
        this.right = right;
    }

    @Override
    public L getLeft() {
        return this.left;
    }

    @Override
    public R getRight() {
        return this.right;
    }

    @Override
    public R setValue(R value) {
        throw new UnsupportedOperationException();
    }
}
