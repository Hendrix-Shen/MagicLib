package top.hendrixshen.magiclib.util.collect;

import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Provider<T> {
    @NotNull
    T get();
}
