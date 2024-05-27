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

/**
 * Reference to <a href="https://github.com/Fallen-Breath/conditional-mixin/blob/88cbb739c375925b134a464428a1f67ee3bd74e2/common/src/main/java/me/fallenbreath/conditionalmixin/api/checker/RestrictionChecker.java">conditional mixin<a/>
 */
public interface MixinDependencyChecker {
    boolean check(String targetClassName, String mixinClassName);

    void setCheckFailureCallback(MixinDependencyCheckFailureCallback callback);
}
