package top.hendrixshen.magiclib.event.render.impl;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;

import lombok.Getter;

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
        this.poseStack.mulPoseMatrix(matrix4f);
        //#else
        //$$ this.poseStack.last().pose().multiply(matrix4f);
        //#endif
    }

    public void enableDepthTest() {
        RenderSystem.enableDepthTest();
    }

    public void disableDepthTest() {
        RenderSystem.disableDepthTest();
    }

    public void depthMask(boolean mask) {
        RenderSystem.depthMask(mask);
    }

    public void enableBlend() {
        RenderSystem.enableBlend();
    }

    public void blendFunc(GlStateManager.SourceFactor srcFactor, GlStateManager.DestFactor dstFactor) {
        RenderSystem.blendFunc(srcFactor, dstFactor);
    }

    public void color4f(float red, float green, float blue, float alpha) {
        //#if MC > 11605
        RenderSystem.setShaderColor(red, green, blue, alpha);
        //#else
        //$$ RenderSystem.color4f(red, green, blue, alpha);
        //#endif
    }

    //#if MC < 11904
    //$$ public void enableTexture() {
    //$$     RenderSystem.enableTexture();
    //$$ }
    //#endif

    //#if MC < 11700
    //$$ public void enableAlphaTest() {
    //$$     RenderSystem.enableAlphaTest();
    //$$ }
    //$$
    //$$ public void disableLighting() {
    //$$    RenderSystem.disableLighting();
    //$$ }
    //#endif
}
