package top.hendrixshen.magiclib.impl.render.context;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore">TweakerMore</a>
 */
public class RenderGlobal {
    public static void enableDepthTest() {
        //#if MC > 11404
        RenderSystem.enableDepthTest();
        //#else
        //$$ GlStateManager.enableDepthTest();
        //#endif
    }

    public static void disableDepthTest() {
        //#if MC >= 11500
        RenderSystem.disableDepthTest();
        //#else
        //$$ GlStateManager.disableDepthTest();
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

    //#if MC < 11700
    public static void enableAlphaTest() {
        //#if MC > 11404
        RenderSystem.enableAlphaTest();
        //#else
        //$$ GlStateManager.enableAlphaTest();
        //#endif
    }

    public static void disableAlphaTest() {
        //#if MC > 11404
        RenderSystem.disableAlphaTest();
        //#else
        //$$ GlStateManager.disableAlphaTest();
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

    public static void depthMask(boolean mask) {
        //#if MC > 11404
        RenderSystem.depthMask(mask);
        //#else
        //$$ GlStateManager.depthMask(mask);
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
}
