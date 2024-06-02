package top.hendrixshen.magiclib.impl.compat.minecraft.network.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ClickEventCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.HoverEventCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.StyleCompat;

//#if MC > 11502
import net.minecraft.network.chat.TextColor;
//#if MC < 11700
import top.hendrixshen.magiclib.mixin.minecraft.accessor.StyleAccessor;
//#endif
//#endif

public class StyleCompatImpl extends AbstractCompat<Style> implements StyleCompat {
    private Style style;

    public StyleCompatImpl(@NotNull Style type) {
        super(type);
        this.style = type;
    }

    @Override
    public @NotNull Style get() {
        return this.style;
    }

    //#if MC > 11605
    @Override
    public StyleCompat withColor(TextColor color) {
        this.style = this.style.withColor(color);
        return this;
    }
    //#endif

    @Override
    public StyleCompat withColor(ChatFormatting arg) {
        //#if MC > 11605
        //$$ this.style = this.style.withColor(arg);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                arg != null ? TextColor.fromLegacyFormat(arg) : null,
                this.style.isBold(),
                this.style.isItalic(),
                this.style.isUnderlined(),
                this.style.isStrikethrough(),
                this.style.isObfuscated(),
                this.style.getClickEvent(),
                this.style.getHoverEvent(),
                this.style.getInsertion(),
                this.style.getFont());
        //#else
        //$$ this.style.setColor(arg);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withBold(boolean bold) {
        //#if MC > 11605
        //$$ this.style = this.style.withBold(bold);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                this.style.getColor(),
                bold,
                this.style.isItalic(),
                this.style.isUnderlined(),
                this.style.isStrikethrough(),
                this.style.isObfuscated(),
                this.style.getClickEvent(),
                this.style.getHoverEvent(),
                this.style.getInsertion(),
                this.style.getFont());
        //#else
        //$$ this.style.setBold(bold);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withItalic(boolean italic) {
        //#if MC > 11605
        //$$ this.style = this.style.withItalic(italic);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                this.style.getColor(),
                this.style.isBold(),
                italic,
                this.style.isUnderlined(),
                this.style.isStrikethrough(),
                this.style.isObfuscated(),
                this.style.getClickEvent(),
                this.style.getHoverEvent(),
                this.style.getInsertion(),
                this.style.getFont());
        //#else
        //$$ this.style.setItalic(italic);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withUnderlined(boolean underlined) {
        //#if MC > 11605
        //$$ this.style = this.style.withUnderlined(underlined);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                this.style.getColor(),
                this.style.isBold(),
                this.style.isItalic(),
                underlined,
                this.style.isStrikethrough(),
                this.style.isObfuscated(),
                this.style.getClickEvent(),
                this.style.getHoverEvent(),
                this.style.getInsertion(),
                this.style.getFont());
        //#else
        //$$ this.style.setUnderlined(underlined);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withStrikethrough(boolean strikethrough) {
        //#if MC > 11605
        //$$ this.style = this.style.withStrikethrough(strikethrough);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                this.style.getColor(),
                this.style.isBold(),
                this.style.isItalic(),
                this.style.isUnderlined(),
                strikethrough,
                this.style.isObfuscated(),
                this.style.getClickEvent(),
                this.style.getHoverEvent(),
                this.style.getInsertion(),
                this.style.getFont());
        //#else
        //$$ this.style.setStrikethrough(strikethrough);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withObfuscated(boolean obfuscated) {
        //#if MC > 11605
        //$$ this.style = this.style.withObfuscated(obfuscated);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                this.style.getColor(),
                this.style.isBold(),
                this.style.isItalic(),
                this.style.isUnderlined(),
                this.style.isStrikethrough(),
                obfuscated,
                this.style.getClickEvent(),
                this.style.getHoverEvent(),
                this.style.getInsertion(),
                this.style.getFont());
        //#else
        //$$ this.style.setObfuscated(obfuscated);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withClickEvent(ClickEvent clickEvent) {
        //#if MC > 11605
        //$$ this.style = this.style.withClickEvent(clickEvent);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                this.style.getColor(),
                this.style.isBold(),
                this.style.isItalic(),
                this.style.isUnderlined(),
                this.style.isStrikethrough(),
                this.style.isObfuscated(),
                clickEvent,
                this.style.getHoverEvent(),
                this.style.getInsertion(),
                this.style.getFont());
        //#else
        //$$ this.style.setClickEvent(clickEvent);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withClickEvent(ClickEventCompat clickEvent) {
        return this.withClickEvent(clickEvent == null ? null : clickEvent.get());
    }

    @Override
    public StyleCompat withHoverEvent(HoverEvent hoverEvent) {
        //#if MC > 11605
        //$$ this.style = style.withHoverEvent(hoverEvent);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                this.style.getColor(),
                this.style.isBold(),
                this.style.isItalic(),
                this.style.isUnderlined(),
                this.style.isStrikethrough(),
                this.style.isObfuscated(),
                this.style.getClickEvent(),
                hoverEvent,
                this.style.getInsertion(),
                this.style.getFont());
        //#else
        //$$ this.style.setHoverEvent(hoverEvent);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withHoverEvent(HoverEventCompat hoverEvent) {
        return this.withHoverEvent(hoverEvent == null ? null : hoverEvent.get());
    }

    @Override
    public StyleCompat withInsertion(String insertion) {
        //#if MC > 11605
        //$$ this.style = this.style.withInsertion(insertion);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                this.style.getColor(),
                this.style.isBold(),
                this.style.isItalic(),
                this.style.isUnderlined(),
                this.style.isStrikethrough(),
                this.style.isObfuscated(),
                this.style.getClickEvent(),
                this.style.getHoverEvent(),
                insertion,
                this.style.getFont());
        //#else
        //$$ this.style.setInsertion(insertion);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withFont(ResourceLocation font) {
        //#if MC > 11605
        //$$ this.style = style.withFont(font);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                this.style.getColor(),
                this.style.isBold(),
                this.style.isItalic(),
                this.style.isUnderlined(),
                this.style.isStrikethrough(),
                this.style.isObfuscated(),
                this.style.getClickEvent(),
                this.style.getHoverEvent(),
                this.style.getInsertion(),
                font);
        //#else
        //$$ // NO-OP
        //#endif
        return this;
    }

    @Override
    public StyleCompat applyFormats(ChatFormatting... chatFormattings) {
        //#if MC > 11502
        this.style = this.style.applyFormats(chatFormattings);
        //#else
        //$$ for(ChatFormatting chatFormatting : chatFormattings) {
        //$$     switch(chatFormatting) {
        //$$         case OBFUSCATED:
        //$$             this.style.setObfuscated(true);
        //$$             break;
        //$$         case BOLD:
        //$$             this.style.setBold(true);
        //$$             break;
        //$$         case STRIKETHROUGH:
        //$$             this.style.setStrikethrough(true);
        //$$             break;
        //$$         case UNDERLINE:
        //$$             this.style.setUnderlined(true);
        //$$             break;
        //$$         case ITALIC:
        //$$             this.style.setItalic(true);
        //$$             break;
        //$$         case RESET:
        //$$             this.style = new Style();
        //$$             return this;
        //$$         default:
        //$$             this.style.setColor(chatFormatting);
        //$$     }
        //$$ }
        //$$
        //#endif
        return this;
    }
}
