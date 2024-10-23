package top.hendrixshen.magiclib.util.minecraft;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.BaseComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.UtilCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.impl.minecraft.MagicLibMinecraft;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.Collection;
import java.util.stream.StreamSupport;

public class MessageUtil {
    private static void sendMessage0(CommandSourceStack source, BaseComponent messages, boolean broadcastToOps) {
        ValueContainer.ofNullable(source).ifPresent(sourceStack ->
                sourceStack.sendSuccess(
                        //#if MC > 11904
                        //$$ () -> messages,
                        //#else
                        messages,
                        //#endif
                        broadcastToOps
                ));
    }

    public static void sendMessage(CommandSourceStack source, BaseComponent message, boolean broadcastToOps) {
        MessageUtil.sendMessage0(source, message, broadcastToOps);
    }

    public static void sendMessageCompat(CommandSourceStack source, @NotNull MutableComponentCompat message, boolean broadcastToOps) {
        MessageUtil.sendMessage0(source, message.get(), broadcastToOps);
    }

    public static void sendMessage(@NotNull Player player, BaseComponent message, boolean broadcastToOps) {
        if (player instanceof ServerPlayer) {
            ServerPlayer serverPlayer = (ServerPlayer) player;
            MessageUtil.sendMessage0(serverPlayer.createCommandSourceStack(), message, broadcastToOps);
        }
    }

    public static void sendMessageCompat(@NotNull Player player, MutableComponentCompat message, boolean broadcastToOps) {
        MessageUtil.sendMessage(player, message.get(), broadcastToOps);
    }

    public static void sendMessage(CommandSourceStack source, @NotNull BaseComponent message) {
        MessageUtil.sendMessage0(source, message, false);
    }

    public static void sendMessageCompat(CommandSourceStack source, @NotNull MutableComponentCompat message) {
        MessageUtil.sendMessageCompat(source, message, false);
    }

    public static void sendMessage(Player player, @NotNull BaseComponent message) {
        MessageUtil.sendMessage(player, message, false);
    }

    public static void sendMessageCompat(Player player, @NotNull MutableComponentCompat message) {
        MessageUtil.sendMessageCompat(player, message, false);
    }

    public static void sendMessage(CommandSourceStack source, @NotNull Iterable<BaseComponent> messages, boolean broadcastToOps) {
        messages.forEach(message -> MessageUtil.sendMessage(source, message, broadcastToOps));
    }

    public static void sendMessageCompat(CommandSourceStack source, @NotNull Iterable<MutableComponentCompat> messages, boolean broadcastToOps) {
        messages.forEach(message -> MessageUtil.sendMessageCompat(source, message, broadcastToOps));
    }

    public static void sendMessage(Player player, @NotNull Iterable<BaseComponent> messages, boolean broadcastToOps) {
        messages.forEach(message -> MessageUtil.sendMessage(player, message, broadcastToOps));
    }

    public static void sendMessageCompat(Player player, @NotNull Iterable<MutableComponentCompat> messages, boolean broadcastToOps) {
        messages.forEach(message -> MessageUtil.sendMessageCompat(player, message, broadcastToOps));
    }

    public static void sendMessage(CommandSourceStack source, @NotNull Iterable<BaseComponent> messages) {
        MessageUtil.sendMessage(source, messages, false);
    }

    public static void sendMessageCompat(CommandSourceStack source, @NotNull Iterable<MutableComponentCompat> messages) {
        MessageUtil.sendMessageCompat(source, messages, false);
    }

    public static void sendMessage(Player player, @NotNull Iterable<BaseComponent> messages) {
        MessageUtil.sendMessage(player, messages, false);
    }

    public static void sendMessageCompat(Player player, @NotNull Iterable<MutableComponentCompat> messages) {
        MessageUtil.sendMessageCompat(player, messages, false);
    }

    // Component I18n cannot be processed with logger.
    public static void sendToConsole(Component message) {
        MagicLibMinecraft.getInstance().getServer().ifPresent(server ->
                //#if MC > 11802
                //$$ server.sendSystemMessage(message)
                //#else
                server.sendMessage(message
                        //#if MC > 11502
                        , UtilCompat.NIL_UUID
                        //#endif
                )
                //#endif
        );
    }

    public static void sendToConsoleCompat(@NotNull ComponentCompat message) {
        MessageUtil.sendToConsole(message.get());
    }

    public static void sendServerMessage(BaseComponent message) {
        MessageUtil.sendToConsole(message);
        MagicLibMinecraft.getInstance().getServer()
                .ifPresent(server -> server.getPlayerList().getPlayers()
                        .forEach(player -> MessageUtil.sendMessage(player, message)));
    }

    public static void sendServerMessageCompat(@NotNull MutableComponentCompat message) {
        MessageUtil.sendServerMessage(message.get());
    }
}
