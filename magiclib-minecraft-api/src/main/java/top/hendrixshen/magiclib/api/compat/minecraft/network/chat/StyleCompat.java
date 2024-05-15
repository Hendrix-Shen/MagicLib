package top.hendrixshen.magiclib.api.compat.minecraft.network.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.network.chat.StyleCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

//#if MC > 11502
import net.minecraft.network.chat.TextColor;
//#endif

public interface StyleCompat extends Provider<Style> {
    static @NotNull StyleCompat of(@NotNull Style style) {
        return new StyleCompatImpl(style);
    }

    static @NotNull Style empty() {
        //#if MC > 11502
        return Style.EMPTY;
        //#else
        //$$ return new Style();
        //#endif
    }

    //#if MC > 11502
    StyleCompat withColor(TextColor color);
    //#endif

    StyleCompat withColor(ChatFormatting arg);

    StyleCompat withBold(boolean bold);

    StyleCompat withItalic(boolean italic);

    StyleCompat withUnderlined(boolean underlined);

    StyleCompat withStrikethrough(boolean strikethrough);

    StyleCompat withObfuscated(boolean obfuscated);

    StyleCompat withClickEvent(ClickEvent clickEvent);

    StyleCompat withClickEvent(ClickEventCompat clickEvent);

    StyleCompat withHoverEvent(HoverEvent hoverEvent);

    StyleCompat withHoverEvent(HoverEventCompat hoverEvent);

    StyleCompat withInsertion(String insertion);

    StyleCompat withFont(ResourceLocation font);

    StyleCompat applyFormats(ChatFormatting... chatFormattings);
}
