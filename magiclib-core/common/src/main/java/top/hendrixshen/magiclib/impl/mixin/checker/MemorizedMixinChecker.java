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

package top.hendrixshen.magiclib.impl.mixin.checker;

import com.google.common.collect.Maps;
import top.hendrixshen.magiclib.api.mixin.checker.MixinDependencyCheckFailureCallback;
import top.hendrixshen.magiclib.api.mixin.checker.MixinDependencyChecker;

import java.util.Map;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/conditional-mixin/blob/88cbb739c375925b134a464428a1f67ee3bd74e2/common/src/main/java/me/fallenbreath/conditionalmixin/impl/MemorizedRestrictionChecker.java">conditional mixin<a/>
 */
public class MemorizedMixinChecker implements MixinDependencyChecker {
    private final MixinDependencyChecker checker;
    private final Map<String, Boolean> memory = Maps.newConcurrentMap();

    public MemorizedMixinChecker(MixinDependencyChecker checker) {
        this.checker = checker;
    }

    @Override
    public boolean check(String targetClassName, String mixinClassName) {
        Boolean result = this.memory.get(mixinClassName);

        if (result == null) {
            result = this.checker.check(targetClassName, mixinClassName);
            this.memory.put(mixinClassName, result);
        }

        return result;
    }

    @Override
    public void setCheckFailureCallback(MixinDependencyCheckFailureCallback callback) {
        this.checker.setCheckFailureCallback(callback);
    }
}
