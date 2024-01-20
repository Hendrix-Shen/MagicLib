package top.hendrixshen.magiclib.util.collect.tuple;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Objects;

public class MutablePair<L, R> extends Pair<L, R> {
    private static final long serialVersionUID = 2002489358806333911L;
    private static final MutablePair<?, ?>[] EMPTY_ARRAY = {};
    @SuppressWarnings("rawtypes")
    private static final MutablePair NULL = new MutablePair<>(null, null);

    private L left;
    private R right;

    @Contract("_, _ -> new")
    public static <L, R> @NotNull Pair<L, R> of(L left, R right) {
        return new MutablePair<>(Objects.requireNonNull(left), Objects.requireNonNull(right));
    }

    @Contract("_ -> new")
    public static <L, R> @NotNull Pair<L, R> of(Map.Entry<L, R> pair) {
        return pair == null ? MutablePair.nullPair() : new MutablePair<>(pair.getKey(), pair.getValue());
    }

    @Contract("_, _ -> new")
    public static <L, R> @NotNull Pair<L, R> ofNullable(L left, R right) {
        return new MutablePair<>(left, right);
    }

    @Contract("_ -> new")
    public static <L, R> @NotNull Pair<L, R> left(L left) {
        return MutablePair.ofNullable(left, null);
    }

    public static <L, R> @NotNull Pair<L, R> right(R right) {
        return MutablePair.ofNullable(null, right);
    }

    @SuppressWarnings("unchecked")
    public static <L, R> MutablePair<L, R>[] emptyArray() {
        return (MutablePair<L, R>[]) MutablePair.EMPTY_ARRAY;
    }

    @SuppressWarnings("unchecked")
    public static <L, R> MutablePair<L, R> nullPair() {
        return MutablePair.NULL;
    }

    public MutablePair(L left, R right) {
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
    public void setLeft(L left) {
        this.left = left;
    }

    @Override
    public void setRight(R right) {
        this.right = right;
    }

    @Override
    public R setValue(R value) {
        R result = this.getRight();
        this.setRight(value);
        return result;
    }
}
