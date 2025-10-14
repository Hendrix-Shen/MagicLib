package top.hendrixshen.magiclib.mixin.minecraft.accessor;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11502
import org.jetbrains.annotations.Nullable;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12109
//$$ import net.minecraft.network.chat.FontDescription;
//#endif

//#if 12109 > MC && MC > 11502
import net.minecraft.resources.ResourceLocation;
//#endif

//#if MC > 11502
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.TextColor;
//#else
//$$ import net.minecraft.ChatFormatting;
//#endif
// CHECKSTYLE.ON: ImportOrder

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11502
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Invoker;
//#endif
// CHECKSTYLE.ON: ImportOrder

@Mixin(Style.class)
public interface StyleAccessor {
    //#if MC > 11502
    @Invoker(value = "<init>")
    static Style magiclib$invokeConstructor(
            @Nullable TextColor color,
            //#if MC > 12103
            //$$ @Nullable Integer integer,
            //#endif
            @Nullable Boolean bold,
            @Nullable Boolean italic,
            @Nullable Boolean underlined,
            @Nullable Boolean strikethrough,
            @Nullable Boolean obfuscated,
            @Nullable ClickEvent clickEvent,
            @Nullable HoverEvent hoverEvent,
            @Nullable String insertion,
            //#if MC >= 12109
            //$$ FontDescription font
            //#else
            ResourceLocation font
            //#endif
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

    // @formatter:off
    @Accessor("color")
    //#if MC > 11502
    TextColor magiclib$getColor();
    //#else
    //#disable-remap
    //$$ ChatFormatting magiclib$getColor();
    //#enable-remap
    //#endif
    // @formatter:on

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
