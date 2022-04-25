package top.hendrixshen.magiclib.test.compat.minecraft.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import top.hendrixshen.magiclib.compat.minecraft.UtilCompatApi;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.StyleCompatApi;

@Environment(EnvType.CLIENT)
public class TestScreen extends Screen {

    public TestScreen() {
        super(new TextComponent("Test Button Screen"));
    }

    public static void test() {
        Screen screen = new TestScreen();
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidgetCompat(new Button(10, 10, 80, 15,
                //#if MC > 11502
                new TranslatableComponent("test button a").withStyle(ChatFormatting.OBFUSCATED),
                //#else
                //$$ new TranslatableComponent("test button a").withStyle(ChatFormatting.OBFUSCATED).getColoredString(),
                //#endif
                button -> Minecraft.getInstance().setScreen(null))
        );

        addRenderableWidget(new Button(50, 10, 80, 15,
                //#if MC > 11502
                new TranslatableComponent("test button a").withStyle(ChatFormatting.OBFUSCATED),
                //#else
                //$$ new TranslatableComponent("test button a").withStyle(ChatFormatting.OBFUSCATED).getColoredString(),
                //#endif
                button -> Minecraft.getInstance().setScreen(null))
        );

        addRenderableOnlyCompat(new Button(10, 30, 80, 15,
                //#if MC > 11502
                new TranslatableComponent("test button b").withStyle(ChatFormatting.GREEN),
                //#else
                //$$ new TranslatableComponent("test button b").withStyle(ChatFormatting.GREEN).getColoredString(),
                //#endif
                button -> {
                    LocalPlayer player = Minecraft.getInstance().player;
                    assert player != null;
                    player.sendMessage(new TextComponent("just test").withStyle(StyleCompatApi.empty()), UtilCompatApi.NIL_UUID);
                    player.sendMessage(new TextComponent("just test")
                            .withStyle(StyleCompatApi.empty().withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                    new TextComponent("just test hover")))), UtilCompatApi.NIL_UUID);
                    player.sendMessage(new TextComponent("just test item")
                            .append(new ItemStack(Items.DIAMOND, 64).getDisplayName()), UtilCompatApi.NIL_UUID);
                    player.sendMessage(new TextComponent("just test entity")
                            .append(player.getDisplayName()), UtilCompatApi.NIL_UUID);

                    player.blockPosition();
                    player.getBlockX();
                    player.getBlockY();
                    player.getBlockZ();
                }));

        addRenderableOnly(new Button(50, 30, 80, 15,
                //#if MC > 11502
                new TranslatableComponent("test button b").withStyle(ChatFormatting.GREEN),
                //#else
                //$$ new TranslatableComponent("test button b").withStyle(ChatFormatting.GREEN).getColoredString(),
                //#endif
                button -> {
                }));


        addWidgetCompat(new Button(10, 50, 80, 15,
                //#if MC > 11502
                new TranslatableComponent("test button c"),
                //#else
                //$$ new TranslatableComponent("test button c").getColoredString(),
                //#endif
                button -> {
                }));

        addWidget(new Button(50, 50, 80, 15,
                //#if MC > 11502
                new TranslatableComponent("test button c"),
                //#else
                //$$ new TranslatableComponent("test button c").getColoredString(),
                //#endif
                button -> {
                }));


    }
}