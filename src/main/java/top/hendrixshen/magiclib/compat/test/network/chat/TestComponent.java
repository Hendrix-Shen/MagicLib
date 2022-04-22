package top.hendrixshen.magiclib.compat.test.network.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.StyleCompatApi;

public class TestComponent {
    public static void test() {
        Component translatableComponent = new TranslatableComponent("test")
                .append(new TranslatableComponent("test1"))
                .append("")
                .withStyle(StyleCompatApi.empty())
                .withStyle(ChatFormatting.AQUA, ChatFormatting.BLACK)
                .withStyle(ChatFormatting.BLUE);

        Component textComponent = new TextComponent("test")
                .append(new TextComponent(""))
                .append("")
                .withStyle(StyleCompatApi.empty())
                .withStyle(ChatFormatting.AQUA, ChatFormatting.BLACK)
                .withStyle(ChatFormatting.BLUE);
    }
}
