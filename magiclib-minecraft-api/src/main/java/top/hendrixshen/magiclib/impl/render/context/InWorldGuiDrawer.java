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
//#if MC >= 12000
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//$$ import net.minecraft.client.Minecraft;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12000
//$$ import top.hendrixshen.magiclib.mixin.minecraft.accessor.GuiGraphicsAccessor;
//$$ import top.hendrixshen.magiclib.util.minecraft.render.RenderUtil;
//#endif
// CHECKSTYLE.ON: ImportOrder

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/ddc655d68b6d5e34cce387863af1ffe79945befb/src/main/java/me/fallenbreath/tweakermore/util/render/context/InWorldGuiDrawer.java">TweakerMore</a>.
 *
 * <p>
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.21.6+        : subproject 1.21.8</li>
 * </p>
 */
public class InWorldGuiDrawer {
    //#if MC >= 12000
    //$$ @NotNull
    //$$ private final GuiGraphics guiGraphics;
    //$$
    //$$ public InWorldGuiDrawer(PoseStack poseStack) {
    //$$     this.guiGraphics = InWorldGuiDrawer.createGuiGraphics(poseStack);
    //$$ }
    //#endif

    @NotNull
    public GuiComponent getGuiComponent() {
        //#if MC >= 12000
        //$$ return this.guiGraphics;
        //#else
        return new GuiComponent() {
        };
        //#endif
    }

    //#if MC >= 12000
    //$$ private static GuiGraphics createGuiGraphics(PoseStack poseStack) {
    //$$     GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance(), RenderUtil.getBufferSource());
    //$$     ((GuiGraphicsAccessor) guiGraphics).magiclib$setPose(poseStack);
    //$$     return guiGraphics;
    //$$ }
    //#endif
}
