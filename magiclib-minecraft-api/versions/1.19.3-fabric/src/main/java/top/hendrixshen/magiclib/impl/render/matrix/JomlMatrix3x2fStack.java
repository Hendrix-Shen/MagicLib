/*
 * This file is part of the TweakerMore project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2024  Fallen_Breath and contributors
 *
 * TweakerMore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TweakerMore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TweakerMore.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.hendrixshen.magiclib.impl.render.matrix;

import lombok.AllArgsConstructor;
import org.joml.Matrix3x2fStack;
import org.joml.Matrix4f;

import com.mojang.blaze3d.vertex.PoseStack;

import top.hendrixshen.magiclib.api.render.matrix.MatrixStack;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/ddc655d68b6d5e34cce387863af1ffe79945befb/versions/1.19.4/src/main/java/me/fallenbreath/tweakermore/util/render/matrix/Joml3x2fMatrixStack.java">TweakerMore</a>.
 *
 * <li>mc1.14 ~ mc1.19.2: subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.19.3+        : subproject 1.19.3       &lt;--------</li>
 */
@AllArgsConstructor
public class JomlMatrix3x2fStack implements MatrixStack {
    private final Matrix3x2fStack matrixStack;

    public Matrix3x2fStack getRaw() {
        return this.matrixStack;
    }

    @Override
    public PoseStack getPoseStack() {
        throw new UnsupportedOperationException("JomlMatrixStack does not support getPoseStack()");
    }

    @Override
    public void pushMatrix() {
        this.matrixStack.pushMatrix();
    }

    @Override
    public void popMatrix() {
        this.matrixStack.popMatrix();
    }

    @Override
    public void translate(double x, double y, double z) {
        if (z != 0) {
            throw new IllegalArgumentException("z must be 0 in Joml3x2fMatrixStack#translate");
        }

        this.matrixStack.translate((float) x, (float) y);
    }

    @Override
    public void scale(double x, double y, double z) {
        this.matrixStack.scale((float) x, (float) y);
    }

    @Override
    public void mulMatrix(Matrix4f matrix4f) {
        throw new UnsupportedOperationException("JomlMatrix3x2fStack does not support mul()");
    }
}
