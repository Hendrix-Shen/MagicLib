package top.hendrixshen.magiclib.api.compat.minecraft.chat.network;

import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.chat.network.StyleCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface StyleCompat extends Provider<Style> {
    @Contract("_ -> new")
    static @NotNull StyleCompat of(Style style) {
        return new StyleCompatImpl(style);
    }

    @Contract(value = " -> new", pure = true)
    static @NotNull Style empty() {
        //#if MC > 11502
        //$$ return Style.EMPTY;
        //#else
        return new Style();
        //#endif
    }

    StyleCompat withStrikethrough(boolean strikethrough);

    StyleCompat withObfuscated(boolean obfuscated);

    StyleCompat withUnderlined(boolean underlined);
}
