package top.hendrixshen.magiclib.test.compat.minecraft.network.chat;

import net.minecraft.network.chat.Component;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.ComponentCompatApi;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.StyleCompatApi;

public class TestComponent {
    public static void test() {
        Component textComponent = ComponentCompatApi.literal("test")
                .withStyleCompat(StyleCompatApi.empty())
                .withStyle(StyleCompatApi.empty());

        Component translatable = ComponentCompatApi.translatable("test", 0)
                .withStyleCompat(StyleCompatApi.empty())
                .withStyle(StyleCompatApi.empty());
    }
}
