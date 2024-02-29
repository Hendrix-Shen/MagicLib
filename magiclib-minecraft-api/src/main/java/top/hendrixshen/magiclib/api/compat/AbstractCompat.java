package top.hendrixshen.magiclib.api.compat;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.util.collect.Provider;

import java.util.Objects;

public abstract class AbstractCompat<T> implements Provider<T> {
    @NotNull
    private final T type;

    public AbstractCompat(@NotNull T type) {
        Objects.requireNonNull(type, "Target couldn't be null");
        this.type = type;
    }

    @Override
    public @NotNull T get() {
        return this.type;
    }
}
