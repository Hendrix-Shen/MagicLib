package top.hendrixshen.magiclib.compat.test.network.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class TestComponent {
    public static void test() {
        Component translatableComponent = new TranslatableComponent("test")
                .append(new TranslatableComponent("test1"))
                .append("")
                .withStyle(Style.EMPTY)
                .withStyle(ChatFormatting.AQUA, ChatFormatting.BLACK)
                .withStyle(ChatFormatting.BLUE);

        Component textComponent = new TextComponent("test")
                .append(new TextComponent(""))
                .append("")
                .withStyle(Style.EMPTY)
                .withStyle(ChatFormatting.AQUA, ChatFormatting.BLACK)
                .withStyle(ChatFormatting.BLUE);
    }
}
