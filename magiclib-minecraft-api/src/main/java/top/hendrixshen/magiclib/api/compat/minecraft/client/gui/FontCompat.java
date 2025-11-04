package top.hendrixshen.magiclib.api.compat.minecraft.client.gui;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11605
//$$ import com.google.common.collect.ImmutableBiMap;
//#endif

//#if MC > 11502
import net.minecraft.util.FormattedCharSequence;
//#endif

//#if MC > 11404
import com.mojang.math.Matrix4f;
import net.minecraft.client.renderer.MultiBufferSource;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.compat.minecraft.client.gui.FontCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.21.6+        : subproject 1.21.8</li>
 */
// CHECKSTYLE.ON: JavadocStyle
public interface FontCompat extends Provider<Font> {
    static @NotNull FontCompat of(@NotNull Font font) {
        return new FontCompatImpl(font);
    }

    //#if MC > 11605
    //$$ ImmutableBiMap<DisplayMode, Font.DisplayMode> displayModeMappings = ImmutableBiMap.of(
    //$$         FontCompat.DisplayMode.NORMAL, Font.DisplayMode.NORMAL,
    //$$         FontCompat.DisplayMode.SEE_THROUGH, Font.DisplayMode.SEE_THROUGH,
    //$$         FontCompat.DisplayMode.POLYGON_OFFSET, Font.DisplayMode.POLYGON_OFFSET
    //$$ );
    //$$
    //$$ static DisplayMode getCompatMode(Font.DisplayMode displayMode) {
    //$$     return FontCompat.displayModeMappings.inverse().get(displayMode);
    //$$ }
    //$$
    //$$ static Font.DisplayMode getDisplayMode(FontCompat.DisplayMode displayModeCompat) {
    //$$     return FontCompat.displayModeMappings.get(displayModeCompat);
    //$$ }
    //#endif

    int drawInBatch(
            String text,
            float x,
            float y,
            int color,
            boolean dropShadow,
            //#if MC > 11404
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            //#endif
            DisplayMode displayMode,
            int backgroundColor,
            int packedLightCoords
    );

    int drawInBatch(
            String text,
            float x,
            float y,
            int color,
            boolean dropShadow,
            //#if MC > 11404
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            //#endif
            @NotNull DisplayMode displayMode,
            int backgroundColor,
            int packedLightCoords,
            boolean bidirectional
    );

    int drawInBatch(
            @NotNull Component component,
            float x,
            float y,
            int color,
            boolean dropShadow,
            //#if MC > 11404
            Matrix4f matrix4f,
            MultiBufferSource buffer,
            //#endif
            DisplayMode displayMode,
            int backgroundColor,
            int packedLightCoords
    );

    //#if MC > 11502
    int drawInBatch(
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
    //#endif

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
