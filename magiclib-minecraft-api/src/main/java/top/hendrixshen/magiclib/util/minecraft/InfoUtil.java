package top.hendrixshen.magiclib.util.minecraft;

import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.BaseComponent;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

//#if MC > 11802 && MC < 11903
//$$ import net.minecraft.network.chat.Component;
//$$ import net.minecraft.client.gui.chat.ClientChatPreview;
//$$
//#if MC > 11900
//$$ import net.minecraft.Util;
//#endif
//#endif

@Environment(net.fabricmc.api.EnvType.CLIENT)
public class InfoUtil {
    public static void displayClientMessage(BaseComponent component, boolean useActionBar) {
        ValueContainer.ofNullable(Minecraft.getInstance().player).ifPresent(p ->
                p.displayClientMessage(component, useActionBar));
    }

    public static void displayClientMessage(@NotNull MutableComponentCompat component, boolean useActionBar) {
        InfoUtil.displayClientMessage(component.get(), useActionBar);
    }

    public static void displayActionBarMessage(BaseComponent component) {
        InfoUtil.displayClientMessage(component, true);
    }

    public static void displayActionBarMessage(MutableComponentCompat component) {
        InfoUtil.displayClientMessage(component, true);
    }

    public static void displayChatMessage(BaseComponent component) {
        InfoUtil.displayClientMessage(component, false);
    }

    public static void displayChatMessage(MutableComponentCompat component) {
        InfoUtil.displayClientMessage(component, false);
    }

    public static void send(@NotNull String text) {
        if (text.startsWith("/")) {
            InfoUtil.sendCommand(text);
        } else {
            InfoUtil.sendChat(text);
        }
    }

    public static void sendChat(@NotNull String message) {
        ValueContainer.ofNullable(Minecraft.getInstance().player).ifPresent(player -> {
            String realText = message.trim();
            if (!realText.isEmpty()) {
                //#if MC > 11902
                //$$ player.connection.sendChat(message.trim());
                //#elseif MC > 11802
                //$$ player.chatSigned(message, InfoUtil.getSign(message));
                //#else
                player.chat(message.trim());
                //#endif
            }
        });
    }

    public static void sendCommand(@NotNull String command) {
        ValueContainer.ofNullable(Minecraft.getInstance().player).ifPresent(player -> {
            String realText = command.trim();
            if (!realText.isEmpty()) {
                //#if MC > 11902
                //$$ player.connection.sendCommand(command.trim());
                //#elseif MC > 11802
                //$$ player.commandSigned(command, InfoUtil.getSign(command));
                //#else
                player.chat(String.format("/%s", command.trim()));
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
