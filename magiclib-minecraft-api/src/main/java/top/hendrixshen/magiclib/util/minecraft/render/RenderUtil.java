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

package top.hendrixshen.magiclib.util.minecraft.render;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.ApiStatus;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11404
import org.jetbrains.annotations.NotNull;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

// CHECKSTYLE.OFF: ImportOrder
//#if 12100 > MC && MC > 11404
import com.mojang.blaze3d.vertex.Tesselator;
//#endif

//#if MC > 11502
import net.minecraft.util.FormattedCharSequence;
//#endif

//#if MC > 11404
import net.minecraft.client.renderer.MultiBufferSource;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.compat.minecraft.client.gui.FontCompat;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/e8edce20f53a1062c570af99a740fb6db0e73447/src/main/java/me/fallenbreath/tweakermore/util/render/RenderUtil.java">TweakerMore</a>.
 */
public class RenderUtil {
    private static final Font TEXT_RENDERER = Minecraft.getInstance().font;
    public static final int TEXT_HEIGHT = RenderUtil.TEXT_RENDERER.lineHeight;
    public static final int TEXT_LINE_HEIGHT = RenderUtil.TEXT_HEIGHT + 1;

    @Getter
    @Setter(onMethod_ = @ApiStatus.Internal)
    private static float partialTick = 1.0F;

    public static int getRenderWidth(String text) {
        return RenderUtil.TEXT_RENDERER.width(text);
    }

    public static int getRenderWidth(Component text) {
        FontCompat fontCompat = FontCompat.of(RenderUtil.TEXT_RENDERER);
        return fontCompat.width(text);
    }

    public static int getSizeScalingXSign() {
        // Stupid change in 24w21a.
        //#if MC > 12006
        //$$ return 1;
        //#else
        return -1;
        //#endif
    }

    //#if MC > 11502
    public static int getRenderWidth(FormattedCharSequence text) {
        return RenderUtil.TEXT_RENDERER.width(text);
    }
    //#endif

    //#if MC > 11404
    public static MultiBufferSource.@NotNull BufferSource getBufferSource() {
        //#if MC > 12006
        //$$ return Minecraft.getInstance().renderBuffers().bufferSource();
        //#else
        return MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
        //#endif
    }
    //#endif
}
