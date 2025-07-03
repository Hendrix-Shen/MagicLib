/*
 * This file is part of the TweakerMore project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package top.hendrixshen.magiclib.api.render.context;

import net.minecraft.client.gui.GuiComponent;

// CHECKSTYLE.OFF: ImportOrder
//#if 12106 > MC && MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.render.matrix.MatrixStack;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/ddc655d68b6d5e34cce387863af1ffe79945befb/src/main/java/me/fallenbreath/tweakermore/util/render/context/GuiRenderContext.java">TweakerMore</a>.
 */
public interface GuiRenderContext {
    //#if 12106 > MC && MC > 11502
    PoseStack getPoseStack();
    //#endif

    MatrixStack getMatrixStack();

    GuiComponent getGuiComponent();

    void pushMatrix();

    void popMatrix();

    void translate(double x, double y);

    void scale(double x, double y);

    //#if MC < 12106
    void translateDirect(double x, double y, double z);

    void scaleDirect(double x, double y, double z);
    //#endif
}
