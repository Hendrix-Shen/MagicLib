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

package top.hendrixshen.magiclib.impl.mixin;

import lombok.EqualsAndHashCode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.AnnotationNode;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.util.Annotations;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/conditional-mixin/blob/88cbb739c375925b134a464428a1f67ee3bd74e2/common/src/main/java/me/fallenbreath/conditionalmixin/impl/AnnotationCleanerImpl.java">conditional mixin<a/>
 */
@EqualsAndHashCode
public class AnnotationRestorer {
    private final Class<? extends Annotation> annotationClass;

    public AnnotationRestorer(Class<? extends Annotation> annotationClass) {
        this.annotationClass = annotationClass;
    }

    @Nullable
    private AnnotationNode previousAnnotationNode;

    public void preApply(ClassNode targetClass) {
        this.previousAnnotationNode = Annotations.getVisible(targetClass, annotationClass);
    }

    public void postApply(@NotNull ClassNode targetClass) {
        String descriptor = Type.getDescriptor(this.annotationClass);
        List<AnnotationNode> annotationNodes = targetClass.visibleAnnotations;

        if (annotationNodes == null) {
            return;
        }

        int index = -1;

        for (int i = 0; i < annotationNodes.size(); i++) {
            if (descriptor.equals(annotationNodes.get(i).desc)) {
                index = i;
                break;
            }
        }

        if (this.previousAnnotationNode == null && index != -1) {
            annotationNodes.remove(index);
        } else if (this.previousAnnotationNode != null) {
            annotationNodes.set(index, this.previousAnnotationNode);
        }
    }

    public boolean matchAnnotationClass(Class<? extends Annotation> annotationClass) {
        return this.annotationClass == annotationClass;
    }
}
