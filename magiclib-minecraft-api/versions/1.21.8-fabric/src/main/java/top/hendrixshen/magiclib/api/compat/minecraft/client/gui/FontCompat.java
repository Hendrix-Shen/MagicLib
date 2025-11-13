package top.hendrixshen.magiclib.api.compat.minecraft.client.gui;

import com.google.common.collect.ImmutableBiMap;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

import top.hendrixshen.magiclib.impl.compat.minecraft.client.gui.FontCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project)</li>
 * <li>mc1.21.6+        : subproject 1.21.8        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
public interface FontCompat extends Provider<Font> {
    static @NotNull FontCompat of(@NotNull Font font) {
        return new FontCompatImpl(font);
    }

    ImmutableBiMap<DisplayMode, Font.DisplayMode> displayModeMappings = ImmutableBiMap.of(
            DisplayMode.NORMAL, Font.DisplayMode.NORMAL,
            DisplayMode.SEE_THROUGH, Font.DisplayMode.SEE_THROUGH,
            DisplayMode.POLYGON_OFFSET, Font.DisplayMode.POLYGON_OFFSET
    );

    static DisplayMode getCompatMode(Font.DisplayMode displayMode) {
        return FontCompat.displayModeMappings.inverse().get(displayMode);
    }

    static Font.DisplayMode getDisplayMode(DisplayMode displayModeCompat) {
        return FontCompat.displayModeMappings.get(displayModeCompat);
    }

    void drawInBatch(
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
    );

    void drawInBatch(
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
    );

    void drawInBatch(
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
    );

    void drawInBatch(
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
    );

    int width(@NotNull Component component);

    @Getter
    enum DisplayMode {
        NORMAL(false),
        SEE_THROUGH(true),
        POLYGON_OFFSET(true);

        private final boolean seeThrough;

        DisplayMode(boolean seeThrough) {
            this.seeThrough = seeThrough;
        }
    }
}
