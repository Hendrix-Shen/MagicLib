package top.hendrixshen.magiclib.compat.test.client.gui.screens;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

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

            addRenderableWidget(new Button(10, 30, 80, 15,
                    new TranslatableComponent("test button b").withStyle(ChatFormatting.GREEN), button -> {
                LocalPlayer player = Minecraft.getInstance().player;
                assert player != null;
                player.sendMessage(new TextComponent("just test").withStyle(Style.EMPTY), Util.NIL_UUID);
                player.sendMessage(new TextComponent("just test")
                        .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                new TextComponent("just test hover")))), Util.NIL_UUID);
                player.sendMessage(new TextComponent("just test item")
                        .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM,
                                new HoverEvent.ItemStackInfo(new ItemStack(Items.DIAMOND, 64))))), Util.NIL_UUID);
                player.sendMessage(new TextComponent("just test entity")
                        .withStyle(Style.EMPTY.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ENTITY,
                                new HoverEvent.EntityTooltipInfo(player.getType(), player.getUUID(), player.getName())))), Util.NIL_UUID);
            }));

            addRenderableOnly(new Button(10, 50, 80, 15, new TranslatableComponent("test button c")
                    .withStyle(ChatFormatting.RED)
                    , button -> {
            }));

            addWidget(new Button(10, 70, 80, 15, new TranslatableComponent("test button d"), button -> {
            }));


        } catch (Throwable e) {
            e.printStackTrace();
        }


    }
}
