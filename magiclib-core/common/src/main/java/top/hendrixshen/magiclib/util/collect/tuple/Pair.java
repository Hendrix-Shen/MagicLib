package top.hendrixshen.magiclib.util.collect.tuple;

import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;

public abstract class Pair<L, R> implements Map.Entry<L, R>, Comparable<Pair<L, R>>, Serializable {
    private static final long serialVersionUID = 5745829894214636491L;

    public abstract L getLeft();

    public abstract R getRight();

    public void setLeft(L left) {
        throw new UnsupportedOperationException();
    };

    public void setRight(R right) {
        throw new UnsupportedOperationException();
    };

    @Override
    public L getKey() {
        return this.getLeft();
    }

    @Override
    public R getValue() {
        return this.getRight();
    }

    @Override
    public int compareTo(@NotNull Pair<L, R> other) {
        int ret = 0;

        if (this.getLeft() == null) {
            ret--;
        } else if (other.getLeft() == null) {
            ret++;
        }

        if (this.getRight() == null) {
            ret--;
        } else if (other.getRight() == null) {
            ret++;
        }

        return ret;
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
    public int hashCode() {
        return (this.getKey() == null ? 0 : this.getKey().hashCode()) ^
                (this.getValue() == null ? 0 : this.getValue().hashCode());
    }

    @Override
    public String toString() {
        return "(" + this.getLeft() + ',' + this.getRight() + ')';
    }
}
