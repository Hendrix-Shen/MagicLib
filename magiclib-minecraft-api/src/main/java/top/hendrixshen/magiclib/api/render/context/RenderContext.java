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

package top.hendrixshen.magiclib.api.render.context;

import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.GuiComponent;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.render.context.RenderContextImpl;

//#if MC >= 12100
//$$ import top.hendrixshen.magiclib.mixin.minecraft.accessor.TesselatorAccessor;
//#endif

//#if MC > 11904
//$$ import com.mojang.blaze3d.vertex.Tesselator;
//$$ import net.minecraft.client.Minecraft;
//$$ import net.minecraft.client.renderer.MultiBufferSource;
//$$ import top.hendrixshen.magiclib.mixin.minecraft.accessor.GuiGraphicsAccessor;
//#endif

//#if MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/util/render/context/RenderContext.java">TweakerMore</a>
 */
public interface RenderContext {
    static @NotNull RenderContext of(
            //#if MC > 11502
            @NotNull PoseStack poseStack
            //#endif
    ) {
        //#if MC > 11904
        //$$ GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance(),
        //#if MC >= 12100
        //$$         MultiBufferSource.immediate(((TesselatorAccessor) Tesselator.getInstance()).magiclib$getBuffer()));
        //#else
        //$$         MultiBufferSource.immediate(Tesselator.getInstance().getBuilder()));
        //#endif
        //$$ ((GuiGraphicsAccessor) guiGraphics).magiclib$setPose(poseStack);
        //#endif

        return new RenderContextImpl(
                //#if MC > 11904
                //$$ guiGraphics,
                //#endif
                //#if MC > 11502
                poseStack
                //#endif
        );
    }

    //#if MC > 11904
    //$$ static RenderContext of(@NotNull GuiGraphics guiGraphics) {
    //$$ 	return new RenderContextImpl(guiGraphics, guiGraphics.pose());
    //$$ }
    //#endif

    GuiComponent getGuiComponent();

    //#if MC > 11502
    PoseStack getPoseStack();
    //#endif

    void pushPose();

    void popPose();

    void translate(double x, double y, double z);

    void scale(double x, double y, double z);

    void mulPoseMatrix(Matrix4f matrix4f);
}
