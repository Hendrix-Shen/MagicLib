package top.hendrixshen.magiclib.api.compat.minecraft.chat.network;

import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.chat.network.ComponentCompatImpl;
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
        //#if MC > 11802
        //$$ return MutableComponentCompat.of(Component.literal(text));
        //#else
        return MutableComponentCompat.of(new TextComponent(text));
        //#endif
    }

    static @NotNull MutableComponentCompat translatable(String text, Object... objects) {
        //#if MC > 11802
        //$$ return MutableComponentCompat.of(Component.translatable(text, objects));
        //#else
        return MutableComponentCompat.of(new TranslatableComponent(text, objects));
        //#endif
    }

    StyleCompat getStyle();
}
