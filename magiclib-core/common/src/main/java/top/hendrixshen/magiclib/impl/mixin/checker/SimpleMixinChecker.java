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

import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.api.dependency.DependencyCheckException;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.mixin.checker.MixinDependencyCheckFailureCallback;
import top.hendrixshen.magiclib.api.mixin.checker.MixinDependencyChecker;
import top.hendrixshen.magiclib.impl.dependency.DependenciesContainer;
import top.hendrixshen.magiclib.util.DependencyUtil;
import top.hendrixshen.magiclib.util.MiscUtil;
import top.hendrixshen.magiclib.util.collect.InfoNode;
import top.hendrixshen.magiclib.util.mixin.MixinUtil;

import java.util.List;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/conditional-mixin/blob/88cbb739c375925b134a464428a1f67ee3bd74e2/common/src/main/java/me/fallenbreath/conditionalmixin/impl/SimpleRestrictionChecker.java">conditional mixin<a/>
 */
public class SimpleMixinChecker implements MixinDependencyChecker {
    private MixinDependencyCheckFailureCallback failureCallback;

    @Override
    public boolean check(String targetClassName, String mixinClassName) {
        ClassNode targetClassNode = MixinUtil.getClassNode(targetClassName);
        ClassNode mixinClassNode = MixinUtil.getClassNode(mixinClassName);

        if (targetClassNode == null || mixinClassNode == null) {
            return false;
        }

        List<DependenciesContainer<ClassNode>> nodes = DependencyUtil.parseDependencies(mixinClassNode, targetClassNode);

        if (nodes.isEmpty()) {
            return true;
        }

        if (nodes.stream().anyMatch(DependenciesContainer::isSatisfied)) {
            return true;
        }

        InfoNode rootNode = new InfoNode(null, I18n.tr("magiclib.dependency.checker.mixin.title",
                mixinClassName, targetClassName));
        MiscUtil.generateDependencyCheckMessage(nodes, rootNode);
        this.onCheckFailure(targetClassName, mixinClassName, new DependencyCheckException(rootNode.toString()));

        return false;
    }

    @Override
    public void setCheckFailureCallback(MixinDependencyCheckFailureCallback callback) {
        this.failureCallback = callback;
    }

    private void onCheckFailure(String targetClassName, String mixinClassName, DependencyCheckException result) {
        if (this.failureCallback != null) {
            this.failureCallback.callback(targetClassName, mixinClassName, result);
        }
    }
}
