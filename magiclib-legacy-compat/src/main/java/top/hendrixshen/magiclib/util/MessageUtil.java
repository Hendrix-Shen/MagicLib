package top.hendrixshen.magiclib.util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;

import java.util.List;

/**
 * See {@link top.hendrixshen.magiclib.util.minecraft.MessageUtil}
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class MessageUtil {
    public static void sendMessage(CommandSourceStack source, String message) {
        top.hendrixshen.magiclib.util.minecraft.MessageUtil.sendMessage(source, message);
    }

    public static void sendMessage(CommandSourceStack source, Component message) {
        top.hendrixshen.magiclib.util.minecraft.MessageUtil.sendMessage(source, message);
    }

    public static void sendMessage(CommandSourceStack source, List<Component> messages) {
        top.hendrixshen.magiclib.util.minecraft.MessageUtil.sendMessage(source, messages);
    }

    public static void sendServerMessage(MinecraftServer server, String message) {
        top.hendrixshen.magiclib.util.minecraft.MessageUtil.sendServerMessage(server, message);
    }

    public static void sendServerMessage(MinecraftServer server, Component message) {
        top.hendrixshen.magiclib.util.minecraft.MessageUtil.sendServerMessage(server, message);
    }

    public static void sendServerMessage(MinecraftServer server, List<Component> messages) {
        top.hendrixshen.magiclib.util.minecraft.MessageUtil.sendServerMessage(server, messages);
    }
}
