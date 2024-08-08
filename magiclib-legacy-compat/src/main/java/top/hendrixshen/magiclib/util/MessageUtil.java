package top.hendrixshen.magiclib.util;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.util.minecraft.ComponentUtil;

import java.util.List;
import java.util.stream.Collectors;

/**
 * See {@link top.hendrixshen.magiclib.util.minecraft.MessageUtil}
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
public class MessageUtil {
    public static void sendMessage(CommandSourceStack source, String message) {
        top.hendrixshen.magiclib.util.minecraft.MessageUtil.sendMessage(source, ComponentUtil.simple(message));
    }

    public static void sendMessage(CommandSourceStack source, Component message) {
        top.hendrixshen.magiclib.util.minecraft.MessageUtil.sendMessage(source, (BaseComponent) message);
    }

    public static void sendMessage(CommandSourceStack source, @NotNull List<Component> messages) {
        top.hendrixshen.magiclib.util.minecraft.MessageUtil.sendMessage(source, ComponentUtil.join(
                ComponentUtil.empty(), messages.stream().map(component -> (BaseComponent) component)
                        .collect(Collectors.toList())));
    }

    public static void sendServerMessage(MinecraftServer server, String message) {
        top.hendrixshen.magiclib.util.minecraft.MessageUtil.sendServerMessage(ComponentUtil.simple(message));
    }

    public static void sendServerMessage(MinecraftServer server, Component message) {
        top.hendrixshen.magiclib.util.minecraft.MessageUtil.sendServerMessage((BaseComponent) message);
    }

    public static void sendServerMessage(MinecraftServer server, @NotNull List<Component> messages) {
        top.hendrixshen.magiclib.util.minecraft.MessageUtil.sendServerMessage(ComponentUtil.join(
                ComponentUtil.empty(), messages.stream().map(component -> (BaseComponent) component)
                        .collect(Collectors.toList())));
    }
}
