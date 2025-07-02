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

package top.hendrixshen.magiclib.impl.render.context;

import org.joml.Matrix4f;

import net.minecraft.client.gui.GuiGraphics;

import top.hendrixshen.magiclib.api.render.context.LevelRenderContext;
import top.hendrixshen.magiclib.api.render.matrix.MatrixStack;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/ddc655d68b6d5e34cce387863af1ffe79945befb/versions/1.21.6/src/main/java/me/fallenbreath/tweakermore/util/render/context/MixedRenderContext.java">TweakerMore</a>.
 *
 * <p>
 * For those who needs in-game transformation and guiDrawer drawing (mc1.21.6+) (very hacky)
 * </p>
 *
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project)</li>
 * <li>mc1.21.6+        : subproject 1.21.7        &lt;--------</li>
 */
public class MixedRenderContext implements LevelRenderContext {
    private final LevelRenderContext levelRenderContext;
    private final InWorldGuiDrawer guiDrawer;

    public MixedRenderContext(LevelRenderContext context) {
        this.levelRenderContext = context;
        this.guiDrawer = InWorldGuiDrawer.getInstance();
    }

    public static MixedRenderContext create(LevelRenderContext worldRenderContext) {
        return new MixedRenderContext(worldRenderContext);
    }

    public void renderGuiElements() {
        this.guiDrawer.render();
    }

    public GuiGraphics getGuiComponent() {
        return this.guiDrawer.getGuiGraphics();
    }

    @Override
    public MatrixStack getMatrixStack() {
        return this.levelRenderContext.getMatrixStack();
    }

    @Override
    public void pushMatrix() {
        this.levelRenderContext.pushMatrix();
    }

    @Override
    public void popMatrix() {
        this.levelRenderContext.popMatrix();
    }

    @Override
    public void translate(double x, double y, double z) {
        this.levelRenderContext.translate(x, y, z);
    }

    @Override
    public void scale(double x, double y, double z) {
        this.levelRenderContext.scale(x, y, z);
    }

    @Override
    public void mulPoseMatrix(Matrix4f matrix4f) {
        this.levelRenderContext.mulPoseMatrix(matrix4f);
    }
}
