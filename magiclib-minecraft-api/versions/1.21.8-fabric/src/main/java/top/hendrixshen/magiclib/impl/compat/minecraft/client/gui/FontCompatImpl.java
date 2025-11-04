package top.hendrixshen.magiclib.impl.compat.minecraft.client.gui;

import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.client.gui.FontCompat;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project)</li>
 * <li>mc1.21.6+        : subproject 1.21.8        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
public class FontCompatImpl extends AbstractCompat<Font> implements FontCompat {
    public FontCompatImpl(@NotNull Font type) {
        super(type);
    }

    @Override
    public void drawInBatch(
            String text,
            float x,
            float y,
            int color,
            boolean dropShadow,
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            DisplayMode displayMode,
            int backgroundColor,
            int packedLightCoords
    ) {
        this.get().drawInBatch(
                text,
                x,
                y,
                color,
                dropShadow,
                matrix4f,
                buffer,
                FontCompat.getDisplayMode(displayMode),
                backgroundColor,
                packedLightCoords
        );
    }

    @Override
    public void drawInBatch(
            String text,
            float x,
            float y,
            int color,
            boolean dropShadow,
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            @NotNull DisplayMode displayMode,
            int backgroundColor,
            int packedLightCoords,
            boolean bidirectional
    ) {
        if (bidirectional) {
            text = this.get().bidirectionalShaping(text);
        }

        this.get().drawInBatch(
                text,
                x,
                y,
                color,
                dropShadow,
                matrix4f,
                buffer,
                FontCompat.getDisplayMode(displayMode),
                backgroundColor,
                packedLightCoords
        );
    }

    @Override
    public void drawInBatch(
            @NotNull Component component,
            float x,
            float y,
            int color,
            boolean dropShadow,
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            DisplayMode displayMode,
            int backgroundColor,
            int packedLightCoords
    ) {
        this.get().drawInBatch(
                component,
                x,
                y,
                color,
                dropShadow,
                matrix4f,
                buffer,
                FontCompat.getDisplayMode(displayMode),
                backgroundColor,
                packedLightCoords
        );
    }

    @Override
    public void drawInBatch(
            FormattedCharSequence formattedCharSequence,
            float x,
            float y,
            int color,
            boolean dropShadow,
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            @NotNull DisplayMode displayMode,
            int backgroundColor,
            int packedLightCoords
    ) {
        this.get().drawInBatch(
                formattedCharSequence,
                x,
                y,
                color,
                dropShadow,
                matrix4f,
                buffer,
                FontCompat.getDisplayMode(displayMode),
                backgroundColor,
                packedLightCoords
        );
    }

    @Override
    public int width(@NotNull Component component) {
        return this.get().width(component);
    }
}
