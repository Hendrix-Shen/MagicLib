package top.hendrixshen.magiclib.api.compat.minecraft.client.gui;

import com.google.common.collect.ImmutableBiMap;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.Font.DisplayMode;
import net.minecraft.network.chat.Component;

import top.hendrixshen.magiclib.impl.compat.minecraft.client.gui.FontCompatImpl;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project)</li>
 * <li>mc1.21.6 ~ mc26.1: subproject 1.21.8</li>
 * <li>mc26.2+          : subproject 26.2        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
public interface FontCompat {
    ImmutableBiMap<DisplayMode, DisplayMode> displayModeMappings = ImmutableBiMap.of(
            DisplayMode.NORMAL, Font.DisplayMode.NORMAL,
            DisplayMode.SEE_THROUGH, Font.DisplayMode.SEE_THROUGH,
            DisplayMode.POLYGON_OFFSET, Font.DisplayMode.POLYGON_OFFSET
    );

    static @NotNull FontCompat of(@NotNull Font font) {
        return new FontCompatImpl(font);
    }

    static DisplayMode getCompatMode(Font.DisplayMode displayMode) {
        return FontCompat.displayModeMappings.inverse().get(displayMode);
    }

    static Font.DisplayMode getDisplayMode(DisplayMode displayModeCompat) {
        return FontCompat.displayModeMappings.get(displayModeCompat);
    }

    int width(@NotNull Component component);
}
