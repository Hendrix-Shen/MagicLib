package top.hendrixshen.magiclib.compat.minecraft.api.network.chat;

import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

public interface StyleCompatApi {
    static Style empty() {
        //#if MC >= 11605
        return Style.EMPTY;
        //#else
        //$$ return new Style();
        //#endif
    }

    default Style withStrikethroughCompat(@Nullable Boolean strikethrough) {
        throw new UnImplCompatApiException();
    }

    default Style withObfuscatedCompat(@Nullable Boolean obfuscated) {
        throw new UnImplCompatApiException();
    }

    default Style withUnderlinedCompat(@Nullable Boolean underlined) {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11605
    default Style withStrikethrough(@Nullable Boolean strikethrough) {
        return this.withStrikethroughCompat(strikethrough);
    }

    default Style withObfuscated(@Nullable Boolean obfuscated) {
        return this.withObfuscatedCompat(obfuscated);
    }
    //#endif

    //#if MC <= 11502
    //$$ default Style withUnderlined(@Nullable Boolean underlined) {
    //$$     return this.withUnderlinedCompat(underlined);
    //$$ }
    //#endif
}
