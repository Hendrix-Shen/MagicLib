package top.hendrixshen.magiclib.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

/**
 * See {@link top.hendrixshen.magiclib.util.minecraft.InfoUtil}
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
public class InfoUtil {
    public static void displayClientMessage(Component component, boolean useActionBar) {
        top.hendrixshen.magiclib.util.minecraft.InfoUtil.displayClientMessage(component, useActionBar);
    }

    public static void displayActionBarMessage(Component component) {
        top.hendrixshen.magiclib.util.minecraft.InfoUtil.displayActionBarMessage(component);
    }

    public static void displayChatMessage(Component component) {
        top.hendrixshen.magiclib.util.minecraft.InfoUtil.displayChatMessage(component);
    }

    public static void send(@NotNull String text) {
        top.hendrixshen.magiclib.util.minecraft.InfoUtil.send(text);
    }

    public static void sendChat(@NotNull String message) {
        top.hendrixshen.magiclib.util.minecraft.InfoUtil.sendChat(message);
    }

    public static void sendCommand(@NotNull String command) {
        top.hendrixshen.magiclib.util.minecraft.InfoUtil.sendChat(command);
    }

    //#if MC > 11802 && MC < 11903
    //$$ public static Component getSign(String text) {
    //$$     return top.hendrixshen.magiclib.util.minecraft.InfoUtil.getSign(text);
    //$$ }
    //#endif
}
