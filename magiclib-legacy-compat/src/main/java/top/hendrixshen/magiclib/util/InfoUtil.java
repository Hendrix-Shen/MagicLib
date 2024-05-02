package top.hendrixshen.magiclib.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;

//#if MC > 11802 && MC < 11903
//#if MC > 11900
//$$ import net.minecraft.Util;
//#endif
//$$ import net.minecraft.client.gui.chat.ClientChatPreview;
//$$ import net.minecraft.network.chat.Component;
//#endif

@Environment(EnvType.CLIENT)
public class InfoUtil {
    public static void displayClientMessage(Component component, boolean useActionBar) {
        Optional.ofNullable(Minecraft.getInstance().player).ifPresent(p -> p.displayClientMessage(component, useActionBar));
    }

    public static void displayActionBarMessage(Component component) {
        displayClientMessage(component, true);
    }

    public static void displayChatMessage(Component component) {
        displayClientMessage(component, false);
    }

    public static void send(@NotNull String text) {
        if (text.startsWith("/")) {
            InfoUtil.sendCommand(text);
        } else {
            InfoUtil.sendChat(text);
        }
    }

    public static void sendChat(@NotNull String message) {
        Optional.ofNullable(Minecraft.getInstance().player).ifPresent(player -> {
            String realText = message.trim();
            if (!realText.isEmpty()) {
                //#if MC > 11902
                player.connection.sendChat(message.trim());
                //#elseif MC > 11802
                //$$ player.chatSigned(message, InfoUtil.getSign(message));
                //#else
                //$$ player.chat(message.trim());
                //#endif
            }
        });
    }

    public static void sendCommand(@NotNull String command) {
        Optional.ofNullable(Minecraft.getInstance().player).ifPresent(player -> {
            String realText = command.trim();
            if (!realText.isEmpty()) {
                //#if MC > 11902
                player.connection.sendCommand(command.trim());
                //#elseif MC > 11802
                //$$ player.commandSigned(command, InfoUtil.getSign(command));
                //#else
                //$$ player.chat(String.format("/%s", command.trim()));
                //#endif
            }
        });
    }

    //#if MC > 11802 && MC < 11903
    //$$ public static Component getSign(String text) {
    //$$     ClientChatPreview ccp = new ClientChatPreview(Minecraft.getInstance());
    //#if MC > 11900
    //$$     return Util.mapNullable(ccp.pull(text), ClientChatPreview.Preview::response);
    //#else
    //$$     return ccp.pull(text);
    //#endif
    //$$ }
    //#endif
}
