package top.hendrixshen.magiclib.util.collect.tuple;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class MutableTriple<L, M, R> extends Triple<L, M, R> {
    private static final MutableTriple<?, ?, ?>[] EMPTY_ARRAY = {};
    @SuppressWarnings("rawtypes")
    private static final MutableTriple NULL = new MutableTriple<>(null, null, null);

    private L left;
    private M middle;
    private R right;

    public static <L, M, R> @NotNull MutableTriple<L, M, R> of(L left, M middle, R right) {
        return new MutableTriple<>(Objects.requireNonNull(left, "left"),
                Objects.requireNonNull(middle, "middle"),
                Objects.requireNonNull(right, "right"));
    }

    public static <L, M, R> @NotNull MutableTriple<L, M, R> ofNullable(L left, M middle, R right) {
        return new MutableTriple<>(left, middle, right);
    }

    @SuppressWarnings("unchecked")
    public static <L, M, R> MutableTriple<L, M, R>[] emptyArray() {
        return (MutableTriple<L, M, R>[]) MutableTriple.EMPTY_ARRAY;
    }

    @SuppressWarnings("unchecked")
    public static <L, M, R> MutableTriple<L, M, R> nullPair() {
        return MutableTriple.NULL;
    }

    private MutableTriple(L left, M middle, R right) {
        this.left = left;
        this.middle = middle;
        this.right = right;
    }

    @Override
    public L getLeft() {
        return this.left;
    }

    @Override
    public M getMiddle() {
        return this.middle;
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
    public void setMiddle(M middle) {
        this.middle = middle;
    }

    @Override
    public void setRight(R right) {
        this.right = right;
    }
}
