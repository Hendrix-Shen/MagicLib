package top.hendrixshen.magiclib.util.collect.tuple;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class ImmutableTriple<L, M, R> extends Triple<L, M, R> {
    private static final ImmutableTriple<?, ?, ?>[] EMPTY_ARRAY = {};
    @SuppressWarnings("rawtypes")
    private static final ImmutableTriple NULL = new ImmutableTriple<>(null, null, null);

    private final L left;
    private final M middle;
    private final R right;

    public static <L, M, R> @NotNull ImmutableTriple<L, M, R> of(L left, M middle, R right) {
        return new ImmutableTriple<>(Objects.requireNonNull(left, "left"),
                Objects.requireNonNull(middle, "middle"),
                Objects.requireNonNull(right, "right"));
    }

    public static <L, M, R> @NotNull ImmutableTriple<L, M, R> ofNullable(L left, M middle, R right) {
        return new ImmutableTriple<>(left, middle, right);
    }

    @SuppressWarnings("unchecked")
    public static <L, M, R> ImmutableTriple<L, M, R>[] emptyArray() {
        return (ImmutableTriple<L, M, R>[]) ImmutableTriple.EMPTY_ARRAY;
    }

    @SuppressWarnings("unchecked")
    public static <L, M, R> ImmutableTriple<L, M, R> nullPair() {
        return ImmutableTriple.NULL;
    }

    private ImmutableTriple(L left, M middle, R right) {
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
}
