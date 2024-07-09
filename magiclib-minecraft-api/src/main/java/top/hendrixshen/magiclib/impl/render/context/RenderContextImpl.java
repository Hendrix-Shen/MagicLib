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

import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.GuiComponent;
import top.hendrixshen.magiclib.api.render.context.RenderContext;
import top.hendrixshen.magiclib.api.render.matrix.MatrixStack;
import org.jetbrains.annotations.NotNull;

//#if MC > 12004
//$$ import top.hendrixshen.magiclib.impl.render.matrix.JomlMatrixStack;
//$$ import org.joml.Matrix4fStack;
//#endif

//#if MC > 11502
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//$$ import org.jetbrains.annotations.NotNull;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/e8edce20f53a1062c570af99a740fb6db0e73447/src/main/java/me/fallenbreath/tweakermore/util/render/context/RenderContextImpl.java">TweakerMore</a>
 */
public class RenderContextImpl implements RenderContext {
    //#if MC > 11904
    //$$ private final GuiGraphics guiGraphics;
    //#endif

    @NotNull
    private final MatrixStack matrixStack;

    public RenderContextImpl(
            //#if MC > 11904
            //$$ GuiGraphics guiGraphics,
            //#endif
            @NotNull MatrixStack matrixStack
    ) {
        //#if MC > 11904
        //$$ this.guiGraphics = guiGraphics;
        //#endif
        this.matrixStack = matrixStack;
    }

    @Override
    public GuiComponent getGuiComponent() {
        //#if MC > 11904
        //$$ return this.guiGraphics;
        //#else
        return new GuiComponent() {
        };
        //#endif
    }

    //#if MC > 11502
    @Override
    public @NotNull MatrixStack getMatrixStack() {
        return this.matrixStack;
    }
    //#endif

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
}
