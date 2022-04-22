package top.hendrixshen.magiclib.compat.test.network.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.StyleCompatApi;


public class TestStyle {
    public static void test() {
        Style style = StyleCompatApi.empty().withColor(ChatFormatting.AQUA)
                .withBold(true)
                .withItalic(true)
                .withUnderlined(true)
                .withStrikethrough(true)
                .withObfuscated(true)
                .withInsertion("test")
                .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("just test")))
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""));
        new ItemStack(Items.DIAMOND).getDisplayName();
    }
}
