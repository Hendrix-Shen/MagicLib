package top.hendrixshen.magiclib.compat.mixin.minecraft.network.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponentCompat;
import net.minecraft.network.chat.Style;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@SuppressWarnings("unused, NullableProblems")
@Mixin(BaseComponent.class)
public abstract class MixinBaseComponent implements MutableComponentCompat {
    @Shadow
    private Style style;

    @Shadow
    public abstract Component setStyle(Style par1);

    @Shadow
    public abstract Style getStyle();

    @Remap("method_27693")
    @Override
    public MutableComponentCompat remap$append(String string) {
        return (MutableComponentCompat) append(string);
    }

    @Remap("method_10852")
    @Override
    public MutableComponentCompat remap$append(Component component) {
        return (MutableComponentCompat) append(component);
    }

    @Remap("method_27696")
    @Override
    public MutableComponentCompat withStyle(Style style) {
        return (MutableComponentCompat) setStyle(style);
    }

    @Remap("method_27692")
    @Override
    public MutableComponentCompat withStyle(ChatFormatting chatFormatting) {
        magicSetStyle(chatFormatting);
        return this;
    }

    private void magicSetStyle(ChatFormatting chatFormatting) {
        if (style == null) {
            style = new Style();
        }
        if (chatFormatting == ChatFormatting.OBFUSCATED) {
            style.setObfuscated(true);
        } else if (chatFormatting == ChatFormatting.BOLD) {
            style.setBold(true);
        } else if (chatFormatting == ChatFormatting.STRIKETHROUGH) {
            style.setStrikethrough(true);
        } else if (chatFormatting == ChatFormatting.UNDERLINE) {
            style.setUnderlined(true);
        } else if (chatFormatting == ChatFormatting.ITALIC) {
            style.setItalic(true);
        } else if (chatFormatting == ChatFormatting.RESET) {
            style = new Style();
        } else {
            style.setColor(chatFormatting);
        }

    }

    @Remap("method_27695")
    @Override
    public MutableComponentCompat withStyle(ChatFormatting[] chatFormattings) {
        for (ChatFormatting chatFormatting : chatFormattings) {
            magicSetStyle(chatFormatting);
        }
        return this;
    }
}
