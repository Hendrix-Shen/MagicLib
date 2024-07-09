/*
 * This file is part of the TweakerMore project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
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

package top.hendrixshen.magiclib.api.render.matrix;

import com.mojang.math.Matrix4f;

//#if MC > 11404
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/6b126681084526b295e268330ca9053dda3b63a9/src/main/java/me/fallenbreath/tweakermore/util/render/matrix/IMatrixStack.java">TweakerMore</a>
 */
public interface MatrixStack {
    //#if MC > 11404
    PoseStack getPoseStack();
    //#endif

    void pushMatrix();

    void popMatrix();

    void translate(double x, double y, double z);

    void scale(double x, double y, double z);

    void mulMatrix(Matrix4f matrix4f);
}
