package top.hendrixshen.magiclib.impl.compat.minecraft.chat.network;

import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.chat.network.StyleCompat;

//#if MC > 11502 && MC < 11700
//$$ import top.hendrixshen.magiclib.mixin.minecraft.accessor.StyleAccessor;
//#endif

public class StyleCompatImpl extends AbstractCompat<Style> implements StyleCompat {
    public StyleCompatImpl(@NotNull Style type) {
        super(type);
    }

    @Override
    public StyleCompat withStrikethrough(boolean strikethrough) {
        //#if MC > 11502
        //$$ Style style = this.get();
        //$$ return new StyleCompatImpl(
        //#if MC > 11605
        //$$         style.withStrikethrough(strikethrough)
        //#else
        //$$         StyleAccessor.magiclib$invokeConstructor(style.getColor(),
        //$$                 style.isBold(),
        //$$                 style.isItalic(),
        //$$                 style.isUnderlined(),
        //$$                 strikethrough,
        //$$                 style.isObfuscated(),
        //$$                 style.getClickEvent(),
        //$$                 style.getHoverEvent(),
        //$$                 style.getInsertion(),
        //$$                 style.getFont())
        //#endif
        //$$ );
        //#else
        this.get().setStrikethrough(strikethrough);
        return this;
        //#endif
    }

    @Override
    public StyleCompat withObfuscated(boolean obfuscated) {
        //#if MC > 11502
        //$$ Style style = this.get();
        //$$ return new StyleCompatImpl(
        //#if MC > 11605
        //$$         style.withObfuscated(obfuscated)
        //#else
        //$$         StyleAccessor.magiclib$invokeConstructor(style.getColor(),
        //$$                 style.isBold(),
        //$$                 style.isItalic(),
        //$$                 style.isUnderlined(),
        //$$                 style.isStrikethrough(),
        //$$                 obfuscated,
        //$$                 style.getClickEvent(),
        //$$                 style.getHoverEvent(),
        //$$                 style.getInsertion(),
        //$$                 style.getFont())
        //#endif
        //$$ );
        //#else
        this.get().setObfuscated(obfuscated);
        return this;
        //#endif
    }

    @Override
    public StyleCompat withUnderlined(boolean underlined) {
        //#if MC > 11502
        //$$ Style style = this.get();
        //$$ return new StyleCompatImpl(
        //#if MC > 11605
        //$$         style.withUnderlined(underlined)
        //#else
        //$$         StyleAccessor.magiclib$invokeConstructor(style.getColor(),
        //$$                 style.isBold(),
        //$$                 style.isItalic(),
        //$$                 underlined,
        //$$                 style.isStrikethrough(),
        //$$                 style.isObfuscated(),
        //$$                 style.getClickEvent(),
        //$$                 style.getHoverEvent(),
        //$$                 style.getInsertion(),
        //$$                 style.getFont())
        //#endif
        //$$ );
        //#else
        this.get().setObfuscated(underlined);
        return this;
        //#endif
    }
}
