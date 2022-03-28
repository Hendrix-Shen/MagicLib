package top.hendrixshen.magiclib.compat.test.network.chat;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Style;

public class TestStyle {
    public static void test() {
        Style style = Style.EMPTY.withColor(ChatFormatting.AQUA)
                .withBold(true)
                .withItalic(true)
                .withUnderlined(true)
                .withStrikethrough(true)
                .withObfuscated(true)
                .withInsertion("test")
                //.withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponent("just test"))) TODO
                .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, ""));
    }
}
