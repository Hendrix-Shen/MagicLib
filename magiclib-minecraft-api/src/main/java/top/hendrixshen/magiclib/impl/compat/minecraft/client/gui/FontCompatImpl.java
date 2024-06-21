package top.hendrixshen.magiclib.impl.compat.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.client.gui.FontCompat;

//#if MC > 11502
import net.minecraft.util.FormattedCharSequence;
//#endif

//#if MC > 11404
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.MultiBufferSource;
import top.hendrixshen.magiclib.api.fake.compat.FontAccessor;
//#else
//$$ import com.mojang.blaze3d.platform.GlStateManager;
//$$ import com.mojang.blaze3d.vertex.BufferBuilder;
//$$ import com.mojang.blaze3d.vertex.DefaultVertexFormat;
//$$ import com.mojang.blaze3d.vertex.Tesselator;
//$$ import org.lwjgl.opengl.GL11;
//$$ import top.hendrixshen.magiclib.impl.render.context.RenderGlobal;
//#endif

@Environment(EnvType.CLIENT)
public class FontCompatImpl extends AbstractCompat<Font> implements FontCompat {
    public FontCompatImpl(@NotNull Font type) {
        super(type);
    }

    @Override
    public int drawInBatch(
            String text,
            float x,
            float y,
            int color,
            boolean dropShadow,
            //#if MC > 11404
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            //#endif
            FontCompat.DisplayMode displayMode,
            int backgroundColor,
            int packedLightCoords
    ) {
        return this.drawInBatch(
                text,
                x,
                y,
                color,
                dropShadow,
                //#if MC > 11404
                matrix4f,
                buffer,
                //#endif
                displayMode,
                backgroundColor,
                packedLightCoords,
                this.get().isBidirectional()
        );
    }

    @Override
    public int drawInBatch(
            String text,
            float x,
            float y,
            int color,
            boolean dropShadow,
            //#if MC > 11404
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            //#endif
            FontCompat.@NotNull DisplayMode displayMode,
            int backgroundColor,
            int packedLightCoords,
            boolean bidirectional
    ) {
        //#if MC > 11502
        return ((FontAccessor) this.get()).magiclib$drawInternal(
                text,
                x,
                y,
                color,
                dropShadow,
                matrix4f,
                buffer,
                //#if MC > 11903
                //$$ FontCompat.getDisplayMode(displayMode),
                //#else
                displayMode.isSeeThrough(),
                //#endif
                backgroundColor,
                packedLightCoords,
                bidirectional
        );
        //#elseif MC > 11404
        //$$ return this.get().drawInBatch(
        //$$         text,
        //$$         x,
        //$$         y,
        //$$         color,
        //$$         dropShadow,
        //$$         matrix4f,
        //$$         buffer,
        //$$         displayMode.isSeeThrough(),
        //$$         backgroundColor,
        //$$         packedLightCoords
        //$$ );
        //#else
        //$$ Tesselator tesselator = Tesselator.getInstance();
        //$$
        //$$ if (displayMode.isSeeThrough()) {
        //$$     RenderGlobal.disableDepthTest();
        //$$ } else {
        //$$     RenderGlobal.enableDepthTest();
        //$$ }
        //$$
        //$$ RenderGlobal.enableBlend();
        //$$ RenderGlobal.blendFuncSeparate(
        //$$         GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA,
        //$$         GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO
        //$$ );
        //$$ RenderGlobal.disableTexture();
        //$$ float a = (float) (backgroundColor >> 24 & 255) / 255.0F;
        //$$ float r = (float) (backgroundColor >> 16 & 255) / 255.0F;
        //$$ float g = (float) (backgroundColor >> 8 & 255) / 255.0F;
        //$$ float b = (float) (backgroundColor & 255) / 255.0F;
        //$$ BufferBuilder bufferBuilder = tesselator.getBuilder();
        //$$ bufferBuilder.begin(GL11.GL_QUADS, DefaultVertexFormat.POSITION_COLOR);
        //$$ bufferBuilder.vertex(x - 1, y - 1, 0.01F).color(r, g, b, a).endVertex();
        //$$ bufferBuilder.vertex(x - 1, y + 9, 0.01F).color(r, g, b, a).endVertex();
        //$$ bufferBuilder.vertex(x + this.get().width(text) + 1, y + 9, 0.01F).color(r, g, b, a).endVertex();
        //$$ bufferBuilder.vertex(x + this.get().width(text) + 1, y - 1, 0.01F).color(r, g, b, a).endVertex();
        //$$ tesselator.end();
        //$$ RenderGlobal.enableTexture();
        //$$ int ret;
        //$$
        //$$ if (dropShadow) {
        //$$     ret = this.get().drawShadow(text, x, y, color);
        //$$ } else {
        //$$     ret = this.get().draw(text, x, y, color);
        //$$ }
        //$$
        //$$ GlStateManager.disableBlend();
        //$$ GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        //$$ return ret;
        //#endif
    }

    @Override
    public int drawInBatch(
            @NotNull Component component,
            float x,
            float y,
            int color,
            boolean dropShadow,
            //#if MC > 11404
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            //#endif
            FontCompat.DisplayMode displayMode,
            int backgroundColor,
            int packedLightCoords
    ) {
        return this.drawInBatch(
                //#if MC > 11502
                component.getVisualOrderText(),
                //#else
                //$$ component.getColoredString(),
                //#endif
                x,
                y,
                color,
                dropShadow,
                //#if MC > 11404
                matrix4f,
                buffer,
                //#endif
                displayMode,
                backgroundColor,
                packedLightCoords
        );
    }

    //#if MC > 11502
    @Override
    public int drawInBatch(
            FormattedCharSequence formattedCharSequence,
            float x,
            float y,
            int color,
            boolean dropShadow,
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            FontCompat.@NotNull DisplayMode displayMode,
            int backgroundColor,
            int packedLightCoords
    ) {
        return ((FontAccessor) this.get()).magiclib$drawInternal(
                formattedCharSequence,
                x,
                y,
                color,
                dropShadow,
                matrix4f,
                buffer,
                //#if MC > 11903
                //$$ FontCompat.getDisplayMode(displayMode),
                //#else
                displayMode.isSeeThrough(),
                //#endif
                backgroundColor,
                packedLightCoords
        );
    }
    //#endif

    @Override
    public int width(@NotNull Component component) {
        return this.get().width(
                //#if MC > 11502
                component
                //#else
                //$$ component.getString()
                //#endif
        );
    }
}
