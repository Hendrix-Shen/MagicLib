package top.hendrixshen.magiclib.impl.compat.minecraft.client.gui;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;

import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.client.gui.FontCompat;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project)</li>
 * <li>mc1.21.6 ~ mc26.1: subproject 1.21.8</li>
 * <li>mc26.2+          : subproject 26.2        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
public class FontCompatImpl extends AbstractCompat<Font> implements FontCompat {
    public FontCompatImpl(@NotNull Font type) {
        super(type);
    }

    @Override
    public int width(@NotNull Component component) {
        return this.get().width(component);
    }
}
