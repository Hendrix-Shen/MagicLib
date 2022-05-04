package top.hendrixshen.magiclib.test.compat.minecraft.client;

import net.minecraft.client.Minecraft;

public class TestMinecraft {
    public static void test() {
        Minecraft minecraft = Minecraft.getInstance();
        minecraft.getWindowCompat();
        minecraft.getWindow();
    }
}
