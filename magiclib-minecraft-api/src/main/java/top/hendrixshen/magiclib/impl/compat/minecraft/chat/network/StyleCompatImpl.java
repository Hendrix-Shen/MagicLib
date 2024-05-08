package top.hendrixshen.magiclib.impl.compat.minecraft.chat.network;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.chat.network.StyleCompat;

//#if MC > 11502 && MC < 11700
import net.minecraft.network.chat.TextColor;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.StyleAccessor;
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

    @Override
    public StyleCompat withColor(ChatFormatting arg) {
        //#if MC > 11605
        //$$ this.style = style.withColor(arg);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                arg != null ? TextColor.fromLegacyFormat(arg) : null,
                style.isBold(),
                style.isItalic(),
                style.isUnderlined(),
                style.isStrikethrough(),
                style.isObfuscated(),
                style.getClickEvent(),
                style.getHoverEvent(),
                style.getInsertion(),
                style.getFont());
        //#else
        //$$ this.style.setColor(arg);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withBold(boolean bold) {
        //#if MC > 11605
        //$$ this.style = style.withBold(bold);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                style.getColor(),
                bold,
                style.isItalic(),
                style.isUnderlined(),
                style.isStrikethrough(),
                style.isObfuscated(),
                style.getClickEvent(),
                style.getHoverEvent(),
                style.getInsertion(),
                style.getFont());
        //#else
        //$$ this.style.setBold(bold);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withItalic(boolean italic) {
        //#if MC > 11605
        //$$ this.style = style.withItalic(italic);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                style.getColor(),
                style.isBold(),
                italic,
                style.isUnderlined(),
                style.isStrikethrough(),
                style.isObfuscated(),
                style.getClickEvent(),
                style.getHoverEvent(),
                style.getInsertion(),
                style.getFont());
        //#else
        //$$ this.style.setItalic(italic);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withUnderlined(boolean underlined) {
        //#if MC > 11605
        //$$ this.style = style.withUnderlined(underlined);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                style.getColor(),
                style.isBold(),
                style.isItalic(),
                underlined,
                style.isStrikethrough(),
                style.isObfuscated(),
                style.getClickEvent(),
                style.getHoverEvent(),
                style.getInsertion(),
                style.getFont());
        //#else
        //$$ this.style.setUnderlined(underlined);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withStrikethrough(boolean strikethrough) {
        //#if MC > 11605
        //$$ this.style = style.withStrikethrough(strikethrough);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                style.getColor(),
                style.isBold(),
                style.isItalic(),
                style.isUnderlined(),
                strikethrough,
                style.isObfuscated(),
                style.getClickEvent(),
                style.getHoverEvent(),
                style.getInsertion(),
                style.getFont());
        //#else
        //$$ this.style.setStrikethrough(strikethrough);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withObfuscated(boolean obfuscated) {
        //#if MC > 11605
        //$$ this.style = style.withObfuscated(obfuscated);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                style.getColor(),
                style.isBold(),
                style.isItalic(),
                style.isUnderlined(),
                style.isStrikethrough(),
                obfuscated,
                style.getClickEvent(),
                style.getHoverEvent(),
                style.getInsertion(),
                style.getFont());
        //#else
        //$$ this.style.setObfuscated(obfuscated);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withClickEvent(ClickEvent clickEvent) {
        //#if MC > 11605
        //$$ this.style = style.withClickEvent(clickEvent);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                style.getColor(),
                style.isBold(),
                style.isItalic(),
                style.isUnderlined(),
                style.isStrikethrough(),
                style.isObfuscated(),
                clickEvent,
                style.getHoverEvent(),
                style.getInsertion(),
                style.getFont());
        //#else
        //$$ this.style.setClickEvent(clickEvent);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withHoverEvent(HoverEvent hoverEvent) {
        //#if MC > 11605
        //$$ this.style = style.withHoverEvent(hoverEvent);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                style.getColor(),
                style.isBold(),
                style.isItalic(),
                style.isUnderlined(),
                style.isStrikethrough(),
                style.isObfuscated(),
                style.getClickEvent(),
                hoverEvent,
                style.getInsertion(),
                style.getFont());
        //#else
        //$$ this.style.setHoverEvent(hoverEvent);
        //#endif
        return this;
    }

    @Override
    public StyleCompat withInsertion(String insertion) {
        //#if MC > 11605
        //$$ this.style = style.withInsertion(insertion);
        //#elseif MC > 11502
        this.style = StyleAccessor.magiclib$invokeConstructor(
                style.getColor(),
                style.isBold(),
                style.isItalic(),
                style.isUnderlined(),
                style.isStrikethrough(),
                style.isObfuscated(),
                style.getClickEvent(),
                style.getHoverEvent(),
                insertion,
                style.getFont());
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
                style.getColor(),
                style.isBold(),
                style.isItalic(),
                style.isUnderlined(),
                style.isStrikethrough(),
                style.isObfuscated(),
                style.getClickEvent(),
                style.getHoverEvent(),
                style.getInsertion(),
                font);
        //#else
        //$$ // NO-OP
        //#endif
        return this;
    }
}
