package top.hendrixshen.magiclib.util.collect.tuple;

import java.util.Map;
import java.util.Objects;

public abstract class Pair<L, R> implements Map.Entry<L, R> {
    public abstract L getLeft();

    public abstract R getRight();

    public void setLeft(L left) {
        throw new UnsupportedOperationException();
    }

    public void setRight(R right) {
        throw new UnsupportedOperationException();
    }

    @Override
    public L getKey() {
        return this.getLeft();
    }

    @Override
    public R getValue() {
        return this.getRight();
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(this.getLeft()) ^
                Objects.hashCode(this.getRight());
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (o instanceof Map.Entry<?, ?>) {
            Map.Entry<?, ?> other = (Map.Entry<?, ?>) o;
            return Objects.equals(this.getKey(), other.getKey()) && Objects.equals(this.getValue(), other.getValue());
        }

        return false;
    }

    @Override
    public String toString() {
        return "(" + this.getLeft() + ',' + this.getRight() + ')';
    }
}
