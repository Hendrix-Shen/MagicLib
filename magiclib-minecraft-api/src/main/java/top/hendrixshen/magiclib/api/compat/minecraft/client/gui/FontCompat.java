package top.hendrixshen.magiclib.api.compat.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.client.gui.FontCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

@Environment(EnvType.CLIENT)
public interface FontCompat extends Provider<Font> {
    static @NotNull FontCompat of(@NotNull Font font) {
        return new FontCompatImpl(font);
    }

    int width(@NotNull Component component);

    @Environment(EnvType.CLIENT)
    enum DisplayMode {
        NORMAL,
        SEE_THROUGH,
        POLYGON_OFFSET
    }
}
