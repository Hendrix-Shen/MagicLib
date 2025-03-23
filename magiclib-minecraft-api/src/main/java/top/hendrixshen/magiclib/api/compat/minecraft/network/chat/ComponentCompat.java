package top.hendrixshen.magiclib.api.compat.minecraft.network.chat;

import org.jetbrains.annotations.NotNull;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11802
//$$ import net.minecraft.network.chat.MutableComponent;
//#else
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.compat.minecraft.network.chat.ComponentCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface ComponentCompat extends Provider<Component> {
    static @NotNull ComponentCompat of(@NotNull Component component) {
        return new ComponentCompatImpl(component);
    }

    static @NotNull BaseComponent literal(String text) {
        //#if MC > 11802
        //$$ return Component.literal(text);
        //#else
        return new TextComponent(text);
        //#endif
    }

    static @NotNull MutableComponentCompat literalCompat(String text) {
        return MutableComponentCompat.of(ComponentCompat.literal(text));
    }

    static @NotNull BaseComponent translatable(String text, Object... objects) {
        //#if MC > 11802
        //$$ return Component.translatable(text, objects);
        //#else
        return new TranslatableComponent(text, objects);
        //#endif
    }

    static @NotNull MutableComponentCompat translatableCompat(String text, Object... objects) {
        return MutableComponentCompat.of(ComponentCompat.translatable(text));
    }

    Style getStyle();

    StyleCompat getStyleCompat();
}
