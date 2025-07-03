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

import com.mojang.blaze3d.platform.GlStateManager;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/ddc655d68b6d5e34cce387863af1ffe79945befb/versions/1.14.4/src/main/java/me/fallenbreath/tweakermore/util/render/context/RenderGlobals.java">TweakerMore</a>.
 *
 * <li>mc1.14           : subproject 1.14.4        &lt;--------</li>
 * <li>mc1.15 ~ mc1.21.4: subproject 1.16.5 (main project)</li>
 * <li>mc1.21.5+        : subproject 1.21.5</li>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RenderGlobal {
    public static void disableAlphaTest() {
        GlStateManager.disableAlphaTest();
    }

    public static void enableAlphaTest() {
        GlStateManager.enableAlphaTest();
    }

    public static void disableLighting() {
        GlStateManager.disableLighting();
    }

    public static void enableLighting() {
        GlStateManager.enableLighting();
    }

    public static void disableDepthTest() {
        GlStateManager.disableDepthTest();
    }

    public static void enableDepthTest() {
        GlStateManager.enableDepthTest();
    }

    public static void depthMask(boolean mask) {
        GlStateManager.depthMask(mask);
    }

    public static void enableBlend() {
        GlStateManager.enableBlend();
    }

    public static void disableBlend() {
        GlStateManager.disableBlend();
    }

    public static void blendFunc(GlStateManager.SourceFactor srcFactor, GlStateManager.DestFactor dstFactor) {
        GlStateManager.blendFunc(srcFactor, dstFactor);
    }

    public static void blendFuncSeparate(
            GlStateManager.SourceFactor sourceFactor, GlStateManager.DestFactor destFactor,
            GlStateManager.SourceFactor sourceFactorAlpha, GlStateManager.DestFactor destFactorAlpha
    ) {
        GlStateManager.blendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha);
    }

    public static void blendFuncSeparate(int sourceFactor, int destFactor,
                                         int sourceFactorAlpha, int destFactorAlpha) {
        GlStateManager.blendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha);
    }

    public static void blendFuncForAlpha() {
        RenderGlobal.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    }

    public static void enableTexture() {
        GlStateManager.enableTexture();
    }

    public static void disableTexture() {
        GlStateManager.disableTexture();
    }

    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        GlStateManager.colorMask(red, green, blue, alpha);
    }

    public static void color4f(float red, float green, float blue, float alpha) {
        GlStateManager.color4f(red, green, blue, alpha);
    }

    public static void defaultBlendFunc() {
        RenderGlobal.blendFuncSeparate(
                GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
                GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
        );
    }
}
