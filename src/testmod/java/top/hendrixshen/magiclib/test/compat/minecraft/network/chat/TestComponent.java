package top.hendrixshen.magiclib.test.compat.minecraft.network.chat;

import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.StyleCompatApi;

public class TestComponent {
    public static void test() {
        Component textComponent = new TextComponent("test")
                .withStyleCompat(StyleCompatApi.empty())
                .withStyle(StyleCompatApi.empty());
    }
}
