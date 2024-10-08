package top.hendrixshen.magiclib.util;

import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class CommonUtil {
    public static <T> T make(@NotNull Supplier<T> factory) {
        return factory.get();
    }

    public static <T> T make(T object, @NotNull Consumer<T> initializer) {
        initializer.accept(object);
        return object;
    }
}
