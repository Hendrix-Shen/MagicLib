package top.hendrixshen.magiclib.api.compat.minecraft.chat.network;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.chat.network.StyleCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface StyleCompat extends Provider<Style> {
    static @NotNull StyleCompat of(@NotNull Style style) {
        return new StyleCompatImpl(style);
    }

    static @NotNull Style empty() {
        //#if MC > 11502
        //$$ return Style.EMPTY;
        //#else
        return new Style();
        //#endif
    }

    StyleCompat withColor(ChatFormatting arg);

    StyleCompat withBold(boolean bold);

    StyleCompat withItalic(boolean italic);

    StyleCompat withUnderlined(boolean underlined);

    StyleCompat withStrikethrough(boolean strikethrough);

    StyleCompat withObfuscated(boolean obfuscated);

    StyleCompat withClickEvent(ClickEvent clickEvent);

    StyleCompat withHoverEvent(HoverEvent hoverEvent);

    StyleCompat withInsertion(String insertion);

    StyleCompat withFont(ResourceLocation font);
}
