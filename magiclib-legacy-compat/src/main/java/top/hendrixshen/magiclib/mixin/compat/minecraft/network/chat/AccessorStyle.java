package top.hendrixshen.magiclib.mixin.compat.minecraft.network.chat;

import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * The implementation for mc [1.16.5, ~)
 */
@ApiStatus.Internal
@Mixin(Style.class)
public interface AccessorStyle {
    @Invoker(value = "<init>")
    static Style invokeConstructor(@Nullable TextColor color, @Nullable Boolean bold, @Nullable Boolean italic,
                                   @Nullable Boolean underlined, @Nullable Boolean strikethrough, @Nullable Boolean obfuscated,
                                   @Nullable ClickEvent clickEvent, @Nullable HoverEvent hoverEvent, @Nullable String insertion,
                                   @Nullable ResourceLocation font) {
        throw new AssertionError();
    }
}
