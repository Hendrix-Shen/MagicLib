package top.hendrixshen.magiclib.event.render.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.mojang.math.Matrix4f;

import lombok.Getter;

import top.hendrixshen.magiclib.impl.render.context.RenderGlobal;

@Environment(EnvType.CLIENT)
public class RenderContext {
    @Getter
    private final PoseStack poseStack;

    public RenderContext(PoseStack poseStack) {
        this.poseStack = poseStack;
    }

    public void pushPose() {
        this.poseStack.pushPose();
    }

    public void popPose() {
        this.poseStack.popPose();
    }

    public void translate(double x, double y, double z) {
        this.poseStack.translate(x, y, z);
    }

    public void scale(double x, double y, double z) {
        this.poseStack.scale((float)x, (float)y, (float)z);
    }

    public void mulPoseMatrix(Matrix4f matrix4f) {
        //#if MC > 11605 || MC < 11500
        //$$ this.poseStack.mulPoseMatrix(matrix4f);
        //#else
        this.poseStack.last().pose().multiply(matrix4f);
        //#endif
    }

    public void enableDepthTest() {
        RenderGlobal.enableDepthTest();
    }

    public void disableDepthTest() {
        RenderGlobal.disableDepthTest();
    }

    public void depthMask(boolean mask) {
        RenderGlobal.depthMask(mask);
    }

    public void enableBlend() {
        RenderGlobal.enableBlend();
    }

    //#if MC < 12105
    public void blendFunc(GlStateManager.SourceFactor srcFactor, GlStateManager.DestFactor dstFactor) {
        RenderGlobal.blendFunc(srcFactor, dstFactor);
    }
    //#endif

    //#if MC < 12106
    @Deprecated
    public void color4f(float red, float green, float blue, float alpha) {
        RenderGlobal.color4f(red, green, blue, alpha);
    }
    //#endif

    //#if MC < 11904
    public void enableTexture() {
        RenderGlobal.enableTexture();
    }
    //#endif

    //#if MC < 11700
    public void enableAlphaTest() {
        RenderGlobal.enableAlphaTest();
    }

    public void disableLighting() {
        RenderGlobal.disableLighting();
    }
    //#endif
}
