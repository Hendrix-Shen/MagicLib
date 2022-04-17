package top.hendrixshen.magiclib.compat.mixin.minecraft.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSourceCompat;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedTextCompat;
import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Mixin(Font.class)
public abstract class MixinFont {
    @Shadow
    public abstract int width(String string);

    @Shadow
    public abstract int draw(String string, float x, float y, int color);

    @Shadow
    public abstract int drawShadow(String string, float x, float y, int color);

    @Remap("method_27525")
    public int width(FormattedTextCompat formattedText) {
        return Mth.ceil(this.width(((Component) formattedText).getString()));
    }


    @Remap("method_30882")
    public int drawInBatch(Component component, float x, float y, int color, boolean shadow, Matrix4f matrix4f,
                           MultiBufferSourceCompat multiBufferSource, boolean seeThrough, int backgroundColor, int light) {
        // no support light
        GlStateManager.pushMatrix();
        GlStateManager.multMatrix(matrix4f);
        GlStateManager.disableLighting();
        GlStateManager.depthMask(false);
        if (seeThrough) {
            GlStateManager.disableDepthTest();
        }

        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.disableTexture();

        float a = (float) (backgroundColor >> 24 & 255) / 255.0F;
        float r = (float) (backgroundColor >> 16 & 255) / 255.0F;
        float g = (float) (backgroundColor >> 8 & 255) / 255.0F;
        float b = (float) (backgroundColor & 255) / 255.0F;

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(7, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertex(x - 1, y - 1, 0).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(x - 1, y + 8, 0).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(x + this.width((FormattedTextCompat) component) + 1, y + 8, 0).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(x + this.width((FormattedTextCompat) component) + 1, y - 1, 0).color(r, g, b, a).endVertex();
        tesselator.end();
        GlStateManager.enableTexture();
        int ret;
        if (shadow) {
            ret = this.drawShadow(component.getColoredString(), x, y, color);
        } else {
            ret = this.draw(component.getColoredString(), x, y, color);
        }
        if (seeThrough) {
            GlStateManager.enableDepthTest();
        }
        GlStateManager.enableLighting();
        GlStateManager.disableBlend();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.popMatrix();
        return ret;
    }
}
