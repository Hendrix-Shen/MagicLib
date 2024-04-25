package top.hendrixshen.magiclib.impl.compat.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.client.gui.FontCompat;

@Environment(EnvType.CLIENT)
public class FontCompatImpl extends AbstractCompat<Font> implements FontCompat {
    public FontCompatImpl(@NotNull Font type) {
        super(type);
    }

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
