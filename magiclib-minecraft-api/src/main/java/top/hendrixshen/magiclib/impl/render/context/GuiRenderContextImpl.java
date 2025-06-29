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

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiComponent;

// CHECKSTYLE.OFF: ImportOrder
//#if 12106 > MC && MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.render.context.GuiRenderContext;
import top.hendrixshen.magiclib.api.render.matrix.MatrixStack;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12106
//$$ import top.hendrixshen.magiclib.impl.render.matrix.JomlMatrix3x2fStack;
//#else
import top.hendrixshen.magiclib.impl.render.matrix.MinecraftPoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/ddc655d68b6d5e34cce387863af1ffe79945befb/src/main/java/me/fallenbreath/tweakermore/util/render/context/GuiRenderContextImpl.java#L55">TweakerMore</a>.
 */
public class GuiRenderContextImpl implements GuiRenderContext {
    //#if MC >= 12000
    //$$ @NotNull
    //$$ private final GuiGraphics guiGraphics;
    //#endif

    @NotNull
    //#if MC >= 12000
    //$$ private final MatrixStack matrixStack;
    //#else
    private final MinecraftPoseStack matrixStack;
    //#endif

    //#if MC >= 12000
    //$$ public GuiRenderContextImpl(@NotNull GuiGraphics guiGraphics, @NotNull MatrixStack matrixStack) {
    //$$     this.guiGraphics = guiGraphics;
    //$$     this.matrixStack = matrixStack;
    //$$ }
    //$$
    //$$ public GuiRenderContextImpl(@NotNull GuiGraphics guiGraphics) {
    //$$     this(
    //$$             guiGraphics,
    //$$             //#if MC >= 12106
    //$$             //$$ new JomlMatrix3x2fStack(guiGraphics.pose())
    //$$             //#else
    //$$             new MinecraftPoseStack(guiGraphics.pose())
    //$$             //#endif
    //$$     );
    //$$ }
    //#else
    public GuiRenderContextImpl(@NotNull MinecraftPoseStack matrixStack) {
        this.matrixStack = matrixStack;
    }
    //#endif

    @Override
    public GuiComponent getGuiComponent() {
        //#if MC > 11904
        //$$ return this.guiGraphics;
        //#else
        return new GuiComponent() {
        };
        //#endif
    }

    //#if 12106 > MC && MC > 11502
    @Override
    public PoseStack getPoseStack() {
        return this.matrixStack.getPoseStack();
    }
    //#endif

    @Override
    public MatrixStack getMatrixStack() {
        return this.matrixStack;
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
    public void translate(double x, double y) {
        this.matrixStack.translate(x, y, 0);
    }

    @Override
    public void scale(double x, double y) {
        this.matrixStack.scale(x, y, 1);
    }

    //#if MC < 12106
    @Override
    public void translateDirect(double x, double y, double z) {
        this.matrixStack.translate(x, y, z);
    }

    @Override
    public void scaleDirect(double x, double y, double z) {
        this.matrixStack.translate(x, y, z);
    }
    //#endif
}
