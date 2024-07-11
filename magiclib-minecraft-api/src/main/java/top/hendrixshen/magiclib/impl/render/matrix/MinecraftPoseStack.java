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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Matrix4f;
import lombok.AllArgsConstructor;
import top.hendrixshen.magiclib.api.render.matrix.MatrixStack;

//#if MC > 11404
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/e8edce20f53a1062c570af99a740fb6db0e73447/src/main/java/me/fallenbreath/tweakermore/util/render/matrix/McMatrixStack.java">TweakerMore</a>
 */
//#if MC > 11502
@AllArgsConstructor
//#if MC < 11700
@SuppressWarnings("deprecation")
//#endif
//#endif
public class MinecraftPoseStack implements MatrixStack {
    //#if MC > 11502
    private final PoseStack poseStack;
    //#endif

    //#if MC > 11404
    @Override
    public PoseStack getPoseStack() {
        //#if MC > 11502
        return this.poseStack;
        //#else
        //$$ throw new RuntimeException("MinecraftPoseStack < mc1.16 does not support getPoseStack()");
        //#endif
    }
    //#endif

    @Override
    public void pushMatrix() {
        //#if MC > 11605
        //$$ this.poseStack.pushPose();
        //#elseif MC > 11404
        RenderSystem.pushMatrix();
        //#else
        //$$ GlStateManager.pushMatrix();
        //#endif
    }

    @Override
    public void popMatrix() {
        //#if MC > 11605
        //$$ this.poseStack.popPose();
        //#elseif MC > 11404
        RenderSystem.popMatrix();
        //#else
        //$$ GlStateManager.popMatrix();
        //#endif
    }

    @Override
    public void translate(double x, double y, double z) {
        //#if MC > 11605
        //$$ this.poseStack.translate(x, y, z);
        //#elseif MC > 11404
        RenderSystem.translated(x, y, z);
        //#else
        //$$ GlStateManager.translated(x, y, z);
        //#endif
    }

    @Override
    public void scale(double x, double y, double z) {
        //#if MC > 11605
        //$$ this.poseStack.scale((float) x, (float) y, (float) z);
        //#elseif MC > 11404
        RenderSystem.scaled(x, y, z);
        //#else
        //$$ GlStateManager.scaled(x, y, z);
        //#endif
    }

    @Override
    public void mulMatrix(Matrix4f matrix4f) {
        //#if MC > 11605
        //$$ this.poseStack.mulPoseMatrix(matrix4f);
        //#elseif MC > 11404
        RenderSystem.multMatrix(matrix4f);
        //#else
        //$$ GlStateManager.multMatrix(matrix4f);
        //#endif
    }
}
