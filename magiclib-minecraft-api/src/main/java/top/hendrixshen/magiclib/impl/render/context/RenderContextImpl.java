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

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.GuiComponent;
import top.hendrixshen.magiclib.api.render.context.RenderContext;

//#if MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
import org.jetbrains.annotations.NotNull;
//#else
//$$ import com.mojang.blaze3d.platform.GlStateManager;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/util/render/context/RenderContextImpl.java">TweakerMore</a>
 */
//#if MC > 11502 && MC < 11700
@SuppressWarnings("deprecation")
//#endif
public class RenderContextImpl implements RenderContext {
    //#if MC > 11904
    //$$ private final GuiGraphics guiGraphics;
    //#endif

    //#if MC > 11502
    @NotNull
    private final PoseStack poseStack;
    //#endif

    public RenderContextImpl(
            //#if MC > 11904
            //$$ GuiGraphics guiGraphics,
            //#endif
            //#if MC > 11502
            @NotNull PoseStack poseStack
            //#endif
    ) {
        //#if MC > 11904
        //$$ this.guiGraphics = guiGraphics;
        //#endif
        //#if MC > 11502
        this.poseStack = poseStack;
        //#endif
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
    public @NotNull PoseStack getPoseStack() {
        return this.poseStack;
    }
    //#endif

    @Override
    public void pushPose() {
        //#if MC > 11605
        //$$ this.poseStack.pushPose();
        //#elseif MC > 11404
        RenderSystem.pushMatrix();
        //#else
        //$$ GlStateManager.pushMatrix();
        //#endif
    }

    @Override
    public void popPose() {
        //#if MC > 11605
        //$$ this.poseStack.popPose();
        //#elseif MC > 11404
        RenderSystem.pushMatrix();
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
    public void mulPoseMatrix(Matrix4f matrix4f) {
        //#if MC > 11605
        //$$ this.poseStack.mulPoseMatrix(matrix4f);
        //#elseif MC > 11404
        RenderSystem.multMatrix(matrix4f);
        //#else
        //$$ GlStateManager.multMatrix(matrix4f);
        //#endif
    }
}
