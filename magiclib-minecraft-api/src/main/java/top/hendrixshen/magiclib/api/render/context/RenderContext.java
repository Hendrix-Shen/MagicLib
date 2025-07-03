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

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12004
//$$ import org.joml.Matrix4fStack;
//#endif

//#if MC > 11902
//$$ import org.joml.Matrix4f;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.client.gui.GuiComponent;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 11903
import com.mojang.math.Matrix4f;
//#endif

//#if MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.render.context.GuiRenderContextImpl;
import top.hendrixshen.magiclib.impl.render.context.LevelRenderContextImpl;
import top.hendrixshen.magiclib.impl.render.matrix.MinecraftPoseStack;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12106
import top.hendrixshen.magiclib.impl.render.context.RenderContextImpl;
//#endif

//#if MC > 12004
//$$ import top.hendrixshen.magiclib.impl.render.matrix.JomlMatrixStack;
//#endif

//#if 12106 > MC && MC > 11904
//$$ import top.hendrixshen.magiclib.util.minecraft.render.RenderContextUtil;
//#endif

//#if MC > 11502
import top.hendrixshen.magiclib.api.render.matrix.MatrixStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/8af864d8c0070f3237736ea80f5050b726e367e3/src/main/java/me/fallenbreath/tweakermore/util/render/context/RenderContext.java">TweakerMore</a>.
 */
public interface RenderContext {
    static LevelRenderContext level(
            //#if MC >= 12006
            //$$ @NotNull Matrix4fStack matrixStack
            //#elseif MC >= 11600
            @NotNull PoseStack poseStack
            //#endif
    ) {
        return new LevelRenderContextImpl(
                //#if MC >= 12006
                //$$ new JomlMatrixStack(matrixStack)
                //#else
                new MinecraftPoseStack(
                        //#if MC >= 11600
                        poseStack
                        //#endif
                )
                //#endif
        );
    }

    static GuiRenderContext gui(
            //#if MC >= 12000
            //$$ @NotNull GuiGraphics guiGraphics
            //#elseif MC >= 11600
            @NotNull PoseStack poseStack
            //#endif
    ) {
        return new GuiRenderContextImpl(
                //#if MC >= 12000
                //$$ guiGraphics
                //#else
                new MinecraftPoseStack(
                        //#if MC >= 11600
                        poseStack
                        //#endif
                )
                //#endif
        );
    }

    //#if MC >= 12000
    //$$ static GuiRenderContext gui(@NotNull GuiGraphics guiGraphics, @NotNull PoseStack poseStack) {
    //$$     return new GuiRenderContextImpl(guiGraphics, new MinecraftPoseStack(poseStack));
    //$$ }
    //#endif

    //#if MC < 12106
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    static @NotNull RenderContext of(
            //#if MC > 11502
            @NotNull PoseStack poseStack
            //#endif
    ) {
        return new RenderContextImpl(
                //#if MC >= 12106
                //$$ RenderContextUtil.createDefaultGuiGraphics(),
                //#elseif MC > 11904
                //$$ RenderContextUtil.createGuiGraphic(poseStack),
                //#endif
                new MinecraftPoseStack(
                        //#if MC > 11502
                        poseStack
                        //#endif
                )
        );
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    static @NotNull LevelRenderContextImpl createWorldRenderContext(
            //#if MC > 12004
            //$$ @NotNull Matrix4fStack matrixStack
            //#elseif MC > 11502
            @NotNull PoseStack matrixStack
            //#endif
    ) {
        return new LevelRenderContextImpl(
                //#if MC >= 12106
                //$$ RenderContextUtil.createDefaultGuiGraphics(),
                //#elseif MC > 11904
                //$$ RenderContextUtil.createGuiGraphic(
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
    //$$ @Deprecated
    //$$ @ApiStatus.ScheduledForRemoval
    //$$ static RenderContext of(@NotNull Matrix4fStack matrixStack) {
    //$$     return new RenderContextImpl(
    //#if MC >= 12106
    //$$             RenderContextUtil.createDefaultGuiGraphics(),
    //#else
    //$$             RenderContextUtil.createGuiGraphic(new PoseStack()),
    //#endif
    //$$             new JomlMatrixStack(matrixStack)
    //$$     );
    //$$ }
    //#endif

    //#if MC > 11904
    //$$ @Deprecated
    //$$ @ApiStatus.ScheduledForRemoval
    //$$ static RenderContext of(@NotNull GuiGraphics guiGraphics) {
    //$$     return new RenderContextImpl(guiGraphics, new MinecraftPoseStack(guiGraphics.pose()));
    //$$ }
    //#endif
    //#endif

    /**
     * @deprecated Moved to {@link GuiRenderContext#getGuiComponent()}.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
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
