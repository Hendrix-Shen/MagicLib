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

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

//#if MC > 11605
//$$ import net.minecraft.client.renderer.ShaderInstance;
//$$ import java.util.function.Supplier;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/util/render/context/RenderGlobals.java">TweakerMore</a>
 */
public class RenderGlobal {
    //#if MC < 11700
    public static void disableAlphaTest() {
        //#if MC > 11404
        RenderSystem.disableAlphaTest();
        //#else
        //$$ GlStateManager.disableAlphaTest();
        //#endif
    }

    public static void enableAlphaTest() {
        //#if MC > 11404
        RenderSystem.enableAlphaTest();
        //#else
        //$$ GlStateManager.enableAlphaTest();
        //#endif
    }

    public static void disableLighting() {
        //#if MC > 11404
        RenderSystem.disableLighting();
        //#else
        //$$ GlStateManager.disableLighting();
        //#endif
    }

    public static void enableLighting() {
        //#if MC > 11404
        RenderSystem.enableLighting();
        //#else
        //$$ GlStateManager.enableLighting();
        //#endif
    }
    //#endif

    public static void disableDepthTest() {
        //#if MC > 11404
        RenderSystem.disableDepthTest();
        //#else
        //$$ GlStateManager.disableDepthTest();
        //#endif
    }

    public static void enableDepthTest() {
        //#if MC > 11404
        RenderSystem.enableDepthTest();
        //#else
        //$$ GlStateManager.enableDepthTest();
        //#endif
    }

    public static void depthMask(boolean mask) {
        //#if MC > 11404
        RenderSystem.depthMask(mask);
        //#else
        //$$ GlStateManager.depthMask(mask);
        //#endif
    }

    public static void enableBlend() {
        //#if MC > 11404
        RenderSystem.enableBlend();
        //#else
        //$$ GlStateManager.enableBlend();
        //#endif
    }

    public static void disableBlend() {
        //#if MC > 11404
        RenderSystem.disableBlend();
        //#else
        //$$ GlStateManager.disableBlend();
        //#endif
    }

    public static void blendFunc(GlStateManager.SourceFactor srcFactor, GlStateManager.DestFactor dstFactor) {
        //#if MC > 11404
        RenderSystem.blendFunc(srcFactor, dstFactor);
        //#else
        //$$ GlStateManager.blendFunc(srcFactor, dstFactor);
        //#endif
    }

    public static void blendFuncSeparate(
            GlStateManager.SourceFactor sourceFactor, GlStateManager.DestFactor destFactor,
            GlStateManager.SourceFactor sourceFactorAlpha, GlStateManager.DestFactor destFactorAlpha
    ) {
        //#if MC > 11404
        RenderSystem.blendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha);
        //#else
        //$$ GlStateManager.blendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha);
        //#endif
    }

    public static void blendFuncSeparate(int sourceFactor, int destFactor,
                                         int sourceFactorAlpha, int destFactorAlpha) {
        //#if MC > 11404
        RenderSystem.blendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha);
        //#else
        //$$ GlStateManager.blendFuncSeparate(sourceFactor, destFactor, sourceFactorAlpha, destFactorAlpha);
        //#endif
    }

    //#if MC < 11904
    public static void enableTexture() {
        //#if MC > 11404
        RenderSystem.enableTexture();
        //#else
        //$$ GlStateManager.enableTexture();
        //#endif
    }

    public static void disableTexture() {
        //#if MC > 11404
        RenderSystem.disableTexture();
        //#else
        //$$ GlStateManager.disableTexture();
        //#endif
    }
    //#endif

    public static void colorMask(boolean red, boolean green, boolean blue, boolean alpha) {
        //#if MC > 11404
        RenderSystem.colorMask(red, green, blue, alpha);
        //#else
        //$$ GlStateManager.colorMask(red, green, blue, alpha);
        //#endif
    }

    public static void color4f(float red, float green, float blue, float alpha) {
        //#if MC > 11605
        //$$ RenderSystem.setShaderColor(red, green, blue, alpha);
        //#elseif MC > 11404
        RenderSystem.color4f(red, green, blue, alpha);
        //#else
        //$$ GlStateManager.color4f(red, green, blue, alpha);
        //#endif
    }

    public static void defaultBlendFunc() {
        //#if MC > 11404
        RenderSystem.defaultBlendFunc();
        //#else
        //$$ RenderGlobal.blendFuncSeparate(
        //$$         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
        //$$         GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
        //$$ );
        //#endif
    }

    //#if MC > 11605
    //$$ public static void setShader(Supplier<ShaderInstance> supplier) {
    //$$     RenderSystem.setShader(supplier);
    //$$ }
    //#endif
}
