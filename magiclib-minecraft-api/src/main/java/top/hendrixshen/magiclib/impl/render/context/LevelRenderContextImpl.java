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

package top.hendrixshen.magiclib.impl.render.context;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiComponent;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 11903
//$$ import org.joml.Matrix4f;
//#else
import com.mojang.math.Matrix4f;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.render.context.LevelRenderContext;
import top.hendrixshen.magiclib.api.render.matrix.MatrixStack;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12004
//$$ import top.hendrixshen.magiclib.impl.render.matrix.JomlMatrixStack;
//#else
import top.hendrixshen.magiclib.impl.render.matrix.MinecraftPoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

public class LevelRenderContextImpl extends RenderContextImpl implements LevelRenderContext {
    @NotNull
    private final MatrixStack matrixStack;

    public LevelRenderContextImpl(
            //#if MC > 11904
            //$$ GuiGraphics guiGraphics,
            //#endif
            //#if MC > 12004
            //$$ @NotNull JomlMatrixStack matrixStack
            //#else
            @NotNull MinecraftPoseStack matrixStack
            //#endif
    ) {
        super(
                //#if MC > 11904
                //$$ guiGraphics,
                //#endif
                matrixStack
        );
        this.matrixStack = matrixStack;
    }

    public LevelRenderContextImpl(@NotNull MatrixStack matrixStack) {
        super(matrixStack);
        this.matrixStack = matrixStack;
    }

    @NotNull
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
    public void translate(double x, double y, double z) {
        this.matrixStack.translate(x, y, z);
    }

    @Override
    public void scale(double x, double y, double z) {
        this.matrixStack.scale(x, y, z);
    }

    @Override
    public void mulPoseMatrix(Matrix4f matrix4f) {
        this.matrixStack.mulMatrix(matrix4f);
    }

    /**
     * @deprecated it shouldn't be here.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @Override
    public GuiComponent getGuiComponent() {
        return super.getGuiComponent();
    }
}
