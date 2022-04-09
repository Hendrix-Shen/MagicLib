package top.hendrixshen.magiclib.compat.mixin.minecraft.network.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.annotation.Public;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@SuppressWarnings({"ConstantConditions, unused"})
@Mixin(Style.class)
public abstract class MixinStyle {

    @Public
    @Remap("field_24360")
    private static final Style EMPTY = new Style() {

        @Override
        public Style setColor(ChatFormatting chatFormatting) {
            return this.copy().setColor(chatFormatting);
        }

        public Style setBold(Boolean boolean_) {
            return this.copy().setBold(boolean_);
        }

        public Style setItalic(Boolean boolean_) {
            return this.copy().setItalic(boolean_);
        }

        public Style setStrikethrough(Boolean boolean_) {
            return this.copy().setStrikethrough(boolean_);
        }

        public Style setUnderlined(Boolean boolean_) {
            return this.copy().setUnderlined(boolean_);
        }

        public Style setObfuscated(Boolean boolean_) {
            return this.copy().setObfuscated(boolean_);
        }

        public Style setClickEvent(ClickEvent clickEvent) {
            return this.copy().setClickEvent(clickEvent);
        }

        public Style setHoverEvent(HoverEvent hoverEvent) {
            return this.copy().setHoverEvent(hoverEvent);
        }
    };

    @Remap("method_30938")
    public Style withUnderlined(@Nullable Boolean boolean_) {
        return ((Style) (Object) this).copy().setUnderlined(boolean_);
    }

    @Remap("method_36140")
    public Style withStrikethrough(@Nullable Boolean boolean_) {
        return ((Style) (Object) this).copy().setStrikethrough(boolean_);
    }

    @Remap("method_36141")
    public Style withObfuscated(@Nullable Boolean boolean_) {
        return ((Style) (Object) this).copy().setObfuscated(boolean_);
    }

}
