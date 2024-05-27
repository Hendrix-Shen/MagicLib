/*
 * This file is part of the conditional mixin project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * conditional mixin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * conditional mixin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with conditional mixin.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.hendrixshen.magiclib.api.mixin.checker;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.mixin.checker.MemorizedMixinChecker;
import top.hendrixshen.magiclib.impl.mixin.checker.SimpleMixinChecker;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/conditional-mixin/blob/88cbb739c375925b134a464428a1f67ee3bd74e2/common/src/main/java/me/fallenbreath/conditionalmixin/api/checker/RestrictionCheckers.java">conditional mixin<a/>
 */
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
