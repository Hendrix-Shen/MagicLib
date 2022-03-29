package top.hendrixshen.magiclib.compat.test.client.gui.screens;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class TestScreen extends Screen {

    public TestScreen() {
        super(new TextComponent("Test Button Screen"));
    }

    @Override
    protected void init() {
        super.init();

        try {
            addRenderableWidget(new Button(10, 10, 80, 15,
                    new TranslatableComponent("test button a").withStyle(ChatFormatting.OBFUSCATED), button -> {
                Minecraft.getInstance().setScreen(null);
            }));

            addRenderableOnly(new Button(10, 30, 80, 15, new TranslatableComponent("test button b")
                    .withStyle(ChatFormatting.RED)
                    , button -> {
            }));

            addWidget(new Button(10, 60, 80, 15, new TranslatableComponent("test button c"), button -> {
            }));


        } catch (Throwable e) {
            e.printStackTrace();
        }


    }
}
