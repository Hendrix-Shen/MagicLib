package top.hendrixshen.magiclib.api.mixin;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.mixin.MemorizedMixinChecker;
import top.hendrixshen.magiclib.impl.mixin.SimpleMixinChecker;

public class MixinDependencyCheckers {
    @Contract(value = " -> new", pure = true)
    public static @NotNull MixinDependencyChecker simple() {
        return new SimpleMixinChecker();
    }

    @Contract(" -> new")
    public static @NotNull MixinDependencyChecker memorized() {
        return MixinDependencyCheckers.memorized(MixinDependencyCheckers.simple());
    }

    @Contract("_ -> new")
    public static @NotNull MixinDependencyChecker memorized(MixinDependencyChecker checker) {
        return new MemorizedMixinChecker(checker);
    }
}
