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
import top.hendrixshen.magiclib.api.render.matrix.MatrixStack;
import top.hendrixshen.magiclib.impl.render.context.LevelRenderContextImpl;
import top.hendrixshen.magiclib.impl.render.context.RenderContextImpl;
import top.hendrixshen.magiclib.impl.render.matrix.MinecraftPoseStack;

//#if MC > 12004
//$$ import org.joml.Matrix4fStack;
//$$ import top.hendrixshen.magiclib.impl.render.matrix.JomlMatrixStack;
//#endif

//#if MC > 11904
//$$ import top.hendrixshen.magiclib.util.minecraft.render.RenderContextUtil;
//#endif

//#if MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/e8edce20f53a1062c570af99a740fb6db0e73447/src/main/java/me/fallenbreath/tweakermore/util/render/context/RenderContext.java">TweakerMore</a>
 */
public interface RenderContext {
    static @NotNull RenderContext of(
            //#if MC > 11502
            @NotNull PoseStack poseStack
            //#endif
    ) {
        return new RenderContextImpl(
                //#if MC > 11904
                //$$ RenderContextUtil.createDrawContext(poseStack),
                //#endif
                new MinecraftPoseStack(
                        //#if MC > 11502
                        poseStack
                        //#endif
                )
        );
    }

    static @NotNull LevelRenderContextImpl createWorldRenderContext(
            //#if MC > 12004
            //$$ @NotNull Matrix4fStack matrixStack
            //#elseif MC > 11502
            @NotNull PoseStack matrixStack
            //#endif
    ) {
        return new LevelRenderContextImpl(
                //#if MC > 11904
                //$$ RenderContextUtil.createDrawContext(
                //#if MC > 12004
                //$$         new PoseStack()
                //#else
                //$$         matrixStack
                //#endif
                //$$ ),
                //#endif
                //#if MC > 12004
                //$$ new JomlMatrixStack(matrixStack)
                //#else
                new MinecraftPoseStack(
                        //#if MC > 11502
                        matrixStack
                        //#endif
                )
                //#endif
        );
    }

    //#if MC > 12004
    //$$ static RenderContext of(@NotNull Matrix4fStack matrixStack) {
    //$$     return new RenderContextImpl(RenderContextUtil.createDrawContext(new PoseStack()), new JomlMatrixStack(matrixStack));
    //$$ }
    //#endif

    //#if MC > 11904
    //$$ static RenderContext of(@NotNull GuiGraphics guiGraphics) {
    //$$ 	return new RenderContextImpl(guiGraphics, new MinecraftPoseStack(guiGraphics.pose()));
    //$$ }
    //#endif

    GuiComponent getGuiComponent();

    //#if MC > 11502
    MatrixStack getMatrixStack();
    //#endif

    void pushMatrix();

    void popMatrix();

    void translate(double x, double y, double z);

    void scale(double x, double y, double z);

    void mulPoseMatrix(Matrix4f matrix4f);
}
