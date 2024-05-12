package top.hendrixshen.magiclib.api.compat.minecraft.network.chat;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.network.chat.ComponentCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

//#if MC < 11900
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
//#endif

public interface ComponentCompat extends Provider<Component> {
    static @NotNull ComponentCompat of(@NotNull Component component) {
        return new ComponentCompatImpl(component);
    }

    static @NotNull MutableComponentCompat literal(String text) {
        return MutableComponentCompat.of(
                //#if MC > 11802
                //$$ Component.literal(text)
                //#else
                new TextComponent(text)
                //#endif
        );
    }

    static @NotNull MutableComponentCompat translatable(String text, Object... objects) {
        return MutableComponentCompat.of(
                //#if MC > 11802
                //$$ Component.translatable(text, objects)
                //#else
                new TranslatableComponent(text, objects)
                //#endif
        );
    }

    StyleCompat getStyle();
}
