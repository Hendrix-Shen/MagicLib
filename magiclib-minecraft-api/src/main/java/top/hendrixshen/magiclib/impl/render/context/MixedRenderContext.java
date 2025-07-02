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

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiComponent;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 11600
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.render.matrix.MinecraftPoseStack;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/ddc655d68b6d5e34cce387863af1ffe79945befb/src/main/java/me/fallenbreath/tweakermore/util/render/context/MixedRenderContext.java">TweakerMore</a>.
 *
 * <p>
 * For those who needs in-game transformation and guiDrawer drawing (mc1.21.6+) (very hacky)
 * </p>
 *
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.21.6+        : subproject 1.21.7</li>
 */
public class MixedRenderContext extends LevelRenderContextImpl {
    private final InWorldGuiDrawer guiDrawer;

    public MixedRenderContext(@NotNull MinecraftPoseStack matrixStack) {
        super(matrixStack);
        this.guiDrawer = new InWorldGuiDrawer(
                //#if MC >= 12000
                //$$ matrixStack.getPoseStack()
                //#endif
        );
    }

    public static MixedRenderContext create() {
        return new MixedRenderContext(new MinecraftPoseStack(
                //#if MC >= 11600
                new PoseStack()
                //#endif
        ));
    }

    //#if 12106 > MC && MC > 11502
    public PoseStack getPoseStack() {
        return ((MinecraftPoseStack) this.getMatrixStack()).getPoseStack();
    }
    //#endif

    @NotNull
    @Override
    public GuiComponent getGuiComponent() {
        return this.guiDrawer.getGuiComponent();
    }
}
