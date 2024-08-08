package top.hendrixshen.magiclib.api.compat.minecraft.network.chat;

import net.minecraft.network.chat.*;
import org.jetbrains.annotations.NotNull;
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
        return MutableComponentCompat.of(
                //#if MC > 11802
                //$$ Component.literal(text)
                //#else
                new TextComponent(text)
                //#endif
        );
    }

    static @NotNull BaseComponent translatable(String text, Object... objects) {
        //#if MC > 11802
        //$$ return Component.translatable(text, objects);
        //#else
        return new TranslatableComponent(text, objects);
        //#endif
    }

    static @NotNull MutableComponentCompat translatableCompat(String text, Object... objects) {
        return MutableComponentCompat.of(
                //#if MC > 11802
                //$$ Component.translatable(text, objects)
                //#else
                new TranslatableComponent(text, objects)
                //#endif
        );
    }

    Style getStyle();

    StyleCompat getStyleCompat();
}
