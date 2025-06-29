package top.hendrixshen.magiclib.util.function;

import com.google.common.base.Suppliers;

import java.util.function.Supplier;

public class MemoizedSupplier<T> implements Supplier<T> {
    private final Supplier<T> supplier;
    private boolean hasValue;

    public MemoizedSupplier(Supplier<T> supplier) {
        this.supplier = Suppliers.memoize(supplier::get);
        this.hasValue = false;
    }

    public T get() {
        this.hasValue = true;
        return this.supplier.get();
    }

    public boolean hasValue() {
        return this.hasValue;
    }
}
