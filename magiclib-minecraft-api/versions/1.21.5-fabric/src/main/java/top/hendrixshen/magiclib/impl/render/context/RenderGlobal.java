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

import com.mojang.blaze3d.opengl.GlConst;
import com.mojang.blaze3d.opengl.GlStateManager;
import com.mojang.blaze3d.platform.DestFactor;
import com.mojang.blaze3d.platform.SourceFactor;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12106
import com.mojang.blaze3d.systems.RenderSystem;
//#endif
// CHECKSTYLE.ON: ImportOrder

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/ddc655d68b6d5e34cce387863af1ffe79945befb/versions/1.21.5/src/main/java/me/fallenbreath/tweakermore/util/render/context/RenderGlobals.java">TweakerMore</a>.
 *
 * <li>mc1.14           : subproject 1.14.4</li>
 * <li>mc1.15 ~ mc1.21.4: subproject 1.16.5 (main project)</li>
 * <li>mc1.21.5+        : subproject 1.21.5        &lt;--------</li>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RenderGlobal {
    public static void disableDepthTest() {
        GlStateManager._disableDepthTest();
    }

    public static void enableDepthTest() {
        GlStateManager._enableDepthTest();
    }

    public static void depthMask(boolean mask) {
        GlStateManager._depthMask(mask);
    }

    public static void enableBlend() {
        GlStateManager._enableBlend();
    }

    public static void disableBlend() {
        GlStateManager._disableBlend();
    }

    public static void blendFuncSeparate(
            SourceFactor sourceFactor, DestFactor destFactor,
            SourceFactor sourceFactorAlpha, DestFactor destFactorAlpha
    ) {
        RenderGlobal.blendFuncSeparate(
                GlConst.toGl(sourceFactor), GlConst.toGl(destFactor),
                GlConst.toGl(sourceFactorAlpha), GlConst.toGl(destFactorAlpha)
        );
    }

    public static void blendFuncSeparate(int sourceFactor, int destFactor,
                                         int sourceFactorAlpha, int destFactorAlpha) {
        GlStateManager._blendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha);
    }

    /**
     * Blends with alpha channel.
     * References:
     * <li>{@link com.mojang.blaze3d.pipeline.BlendFunction#TRANSLUCENT}</li>
     * <li>{@link com.mojang.blaze3d.opengl.GlCommandEncoder#applyPipelineState}</li>
     */
    public static void blendFuncForAlpha() {
        RenderGlobal.blendFuncSeparate(
                SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA,
                SourceFactor.ONE, DestFactor.ONE_MINUS_SRC_ALPHA
        );
    }

    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        GlStateManager._colorMask(red, green, blue, alpha);
    }

    //#if MC < 12106
    @Deprecated
    public static void color4f(float red, float green, float blue, float alpha) {
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }
    //#endif

    public static void defaultBlendFunc() {
        RenderGlobal.blendFuncSeparate(
                SourceFactor.SRC_ALPHA, DestFactor.ONE_MINUS_SRC_ALPHA,
                SourceFactor.ONE, DestFactor.ZERO
        );
    }
}
