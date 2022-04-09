package top.hendrixshen.magiclib.compat.test.network.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TestStyle {
    public static void test() {
        Style style = Style.EMPTY.withColor(ChatFormatting.AQUA)
                .withBold(true)
                .withItalic(true)
                .withUnderlined(true)
                .withStrikethrough(true)
                .withObfuscated(true)
                .withInsertion("test")
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("just test")))
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_ITEM, new HoverEvent.ItemStackInfo(new ItemStack(Items.DIAMOND))))
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""));
    }
}
