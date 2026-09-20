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

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 26.3
//$$ import com.mojang.renderpearl.backend.opengl.GlConst;
//$$ import com.mojang.renderpearl.backend.opengl.GlStateManager;
//#else
import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
//#endif

//#if MC >= 26.3
//$$ import com.mojang.renderpearl.api.pipeline.BlendFactor;
//#else
import com.mojang.blaze3d.platform.BlendFactor;
//#endif

//#if MC >= 26.3
//$$ import com.mojang.renderpearl.api.pipeline.ColorTargetState;
//#else
import com.mojang.blaze3d.pipeline.ColorTargetState;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.render.RenderBackend;

/**
 * Minecraft 26.2 introduced an experimental Vulkan backend which owns no OpenGL context at all, so any
 * immediate state call would crash the game there. Every method that ends up in {@code GlStateManager} is
 * therefore guarded by {@link RenderBackend} and turns into a silent no-op on a non-OpenGL backend, which
 * keeps the OpenGL backend byte-for-byte identical to the previous behavior.
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14           : subproject 1.14.4</li>
 * <li>mc1.15 ~ mc1.21.4: subproject 1.16.5 (main project)</li>
 * <li>mc1.21.5 ~ mc26.1: subproject 1.21.5</li>
 * <li>mc26.2+          : subproject 26.2        &lt;--------</li>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RenderGlobal {
    public static void disableDepthTest() {
        if (!RenderBackend.getCurrent().supportsImmediateState()) {
            return;
        }

        GlStateManager._disableDepthTest();
    }

    public static void enableDepthTest() {
        if (!RenderBackend.getCurrent().supportsImmediateState()) {
            return;
        }

        GlStateManager._enableDepthTest();
    }

    public static void depthMask(boolean mask) {
        if (!RenderBackend.getCurrent().supportsImmediateState()) {
            return;
        }

        GlStateManager._depthMask(mask);
    }

    public static void enableBlend() {
        if (!RenderBackend.getCurrent().supportsImmediateState()) {
            return;
        }

        // The argument is the index of the color attachment to blend, 0 is the main target.
        GlStateManager._enableBlend(0);
    }

    public static void disableBlend() {
        if (!RenderBackend.getCurrent().supportsImmediateState()) {
            return;
        }

        // The argument is the index of the color attachment to stop blending, 0 is the main target.
        GlStateManager._disableBlend(0);
    }

    public static void blendFuncSeparate(
            BlendFactor sourceFactor,
            BlendFactor destFactor,
            BlendFactor sourceFactorAlpha,
            BlendFactor destFactorAlpha
    ) {
        RenderGlobal.blendFuncSeparate(
                GlConst.toGl(sourceFactor),
                GlConst.toGl(destFactor),
                GlConst.toGl(sourceFactorAlpha),
                GlConst.toGl(destFactorAlpha)
        );
    }

    public static void blendFuncSeparate(int sourceFactor, int destFactor,
                                         int sourceFactorAlpha, int destFactorAlpha) {
        if (!RenderBackend.getCurrent().supportsImmediateState()) {
            return;
        }

        GlStateManager._blendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha);
    }

    /**
     * Blends with alpha channel.
     * References:
     * <li>{@code BlendFunction.TRANSLUCENT}</li>
     * <li>{@code GlCommandEncoder#applyPipelineState} of the OpenGL backend</li>
     */
    public static void blendFuncForAlpha() {
        RenderGlobal.blendFuncSeparate(
                BlendFactor.SRC_ALPHA,
                BlendFactor.ONE_MINUS_SRC_ALPHA,
                BlendFactor.ONE,
                BlendFactor.ONE_MINUS_SRC_ALPHA
        );
    }

    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        if (!RenderBackend.getCurrent().supportsImmediateState()) {
            return;
        }

        int colorMask = (red ? ColorTargetState.WRITE_RED : ColorTargetState.WRITE_NONE)
                + (green ? ColorTargetState.WRITE_GREEN : ColorTargetState.WRITE_NONE)
                + (blue ? ColorTargetState.WRITE_BLUE : ColorTargetState.WRITE_NONE)
                + (alpha ? ColorTargetState.WRITE_ALPHA : ColorTargetState.WRITE_NONE);
        GlStateManager._colorMask(colorMask);
    }

    public static void defaultBlendFunc() {
        RenderGlobal.blendFuncSeparate(
                BlendFactor.SRC_ALPHA,
                BlendFactor.ONE_MINUS_SRC_ALPHA,
                BlendFactor.ONE,
                BlendFactor.ZERO
        );
    }
}
