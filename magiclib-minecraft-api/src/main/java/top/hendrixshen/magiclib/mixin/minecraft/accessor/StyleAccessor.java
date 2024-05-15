package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

//#if MC > 11502
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Invoker;
//#else
//$$ import net.minecraft.ChatFormatting;
//#endif

@Mixin(Style.class)
public interface StyleAccessor {
    //#if MC > 11502
    @Invoker(value = "<init>")
    static Style magiclib$invokeConstructor(
            @Nullable TextColor color,
            @Nullable Boolean bold,
            @Nullable Boolean italic,
            @Nullable Boolean underlined,
            @Nullable Boolean strikethrough,
            @Nullable Boolean obfuscated,
            @Nullable ClickEvent clickEvent,
            @Nullable HoverEvent hoverEvent,
            @Nullable String insertion,
            @Nullable ResourceLocation font
    ) {
        throw new AssertionError();
    }
    //#endif

    @Accessor("bold")
    Boolean magiclib$getBold();

    @Accessor("italic")
    Boolean magiclib$getItalic();

    @Accessor("underlined")
    Boolean magiclib$getUnderlined();

    @Accessor("strikethrough")
    Boolean magiclib$strikethrough();

    @Accessor("obfuscated")
    Boolean magiclib$getObfuscated();

    @Accessor("color")
    //#if MC > 11502
    TextColor magiclib$getColor();
    //#else
    //#disable-remap
    //$$ ChatFormatting magiclib$getColor();
    //#enable-remap
    //#endif

    @Accessor("hoverEvent")
    HoverEvent getHoverEvent();

    //#if MC > 11502
    @Mutable
    @Accessor("underlined")
    void magiclib$setUnderlined(Boolean magiclib$setUnderlined);

    @Mutable
    @Accessor("strikethrough")
    void magiclib$setStrikethrough(Boolean strikethrough);

    @Mutable
    @Accessor("obfuscated")
    void magiclib$setObfuscated(Boolean obfuscated);
    //#endif
}
