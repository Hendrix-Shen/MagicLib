package top.hendrixshen.magiclib.api.mixin.checker;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.mixin.checker.MemorizedMixinChecker;
import top.hendrixshen.magiclib.impl.mixin.checker.SimpleMixinChecker;

public class MixinDependencyCheckers {
    /**
     * Creates a SimpleMixinChecker.
     *
     * @return A simple checker.
     */
    @Contract(value = " -> new", pure = true)
    public static @NotNull MixinDependencyChecker simple() {
        return new SimpleMixinChecker();
    }

    /**
     * Creates a memoized version of the {@code MixinDependencyChecker}, initialized with the simple mode.
     * <p>
     * The returned checker will cache the results of the checks to avoid redundant calculations.
     *
     * @return A memoized checker.
     */
    @Contract(" -> new")
    public static @NotNull MixinDependencyChecker memorized() {
        return MixinDependencyCheckers.memorized(MixinDependencyCheckers.simple());
    }

    /**
     * Creates a memoized version of the given {@code MixinDependencyChecker}.
     * <p>
     * The returned checker will cache the results of the checks to avoid redundant calculations.
     *
     * @param checker The checker to be memoized.
     * @return A memoized checker.
     */
    @Contract("_ -> new")
    public static @NotNull MixinDependencyChecker memorized(MixinDependencyChecker checker) {
        return new MemorizedMixinChecker(checker);
    }
}
