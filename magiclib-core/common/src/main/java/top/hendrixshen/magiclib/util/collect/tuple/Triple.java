package top.hendrixshen.magiclib.util.collect.tuple;

import java.util.Objects;

public abstract class Triple<L, M, R> {
    public abstract L getLeft();

    public abstract M getMiddle();

    public abstract R getRight();

    public void setLeft(L left) {
        throw new UnsupportedOperationException();
    }

    public void setMiddle(M middle) {
        throw new UnsupportedOperationException();
    }

    public void setRight(R right) {
        throw new UnsupportedOperationException();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getLeft()) ^
                Objects.hashCode(this.getMiddle()) ^
                Objects.hashCode(this.getRight());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }

        if (obj instanceof Triple<?, ?, ?>) {
            final Triple<?, ?, ?> other = (Triple<?, ?, ?>) obj;
            return Objects.equals(this.getLeft(), other.getLeft())
                    && Objects.equals(this.getMiddle(), other.getMiddle())
                    && Objects.equals(this.getRight(), other.getRight());
        }

        return false;
    }

    @Override
    public String toString() {
        return "(" + this.getLeft() + ',' + this.getMiddle() + ',' + this.getRight() + ')';
    }
}
