package top.hendrixshen.magiclib.util.minecraft;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.UtilCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;
import top.hendrixshen.magiclib.impl.minecraft.MagicLibMinecraft;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.Collection;

public class MessageUtil {
    private static void sendMessage0(CommandSourceStack source, Component messages, boolean broadcastToAdmins) {
        ValueContainer.ofNullable(source).ifPresent(sourceStack ->
                sourceStack.sendSuccess(
                        //#if MC > 11904
                        //$$ () -> messages,
                        //#else
                        messages,
                        //#endif
                        broadcastToAdmins
                ));
    }

    public static void sendMessage(CommandSourceStack source, String message, boolean broadcastToAdmins) {
        MessageUtil.sendMessageCompat(source, ComponentCompat.literal(message), broadcastToAdmins);
    }

    public static void sendMessage(CommandSourceStack source, Component message, boolean broadcastToAdmins) {
        MessageUtil.sendMessage0(source, message, broadcastToAdmins);
    }

    public static void sendMessageCompat(CommandSourceStack source, @NotNull ComponentCompat message,
                                         boolean broadcastToAdmins) {
        MessageUtil.sendMessage0(source, message.get(), broadcastToAdmins);
    }

    public static void sendMessage(CommandSourceStack source, String message) {
        MessageUtil.sendMessage(source, message, false);
    }

    public static void sendMessage(CommandSourceStack source, @NotNull Component message) {
        MessageUtil.sendMessage0(source, message, false);
    }

    public static void sendMessageCompat(CommandSourceStack source, @NotNull ComponentCompat message) {
        MessageUtil.sendMessage0(source, message.get(), false);
    }

    public static void sendMessage(CommandSourceStack source, @NotNull Collection<Component> messages,
                                   boolean broadcastToAdmins) {
        messages.forEach(message -> MessageUtil.sendMessage(source, message, broadcastToAdmins));
    }

    public static void sendMessage(CommandSourceStack source, @NotNull Collection<Component> messages) {
        messages.forEach(message -> MessageUtil.sendMessage(source, message, false));
    }

    public static void sendMessageCompat(CommandSourceStack source, @NotNull Collection<ComponentCompat> messages,
                                         boolean broadcastToAdmins) {
        messages.stream()
                .map(ComponentCompat::get)
                .forEach(message -> MessageUtil.sendMessage(source, message, broadcastToAdmins));
    }

    public static void sendMessageCompat(CommandSourceStack source, @NotNull Collection<ComponentCompat> messages) {
        messages.stream()
                .map(ComponentCompat::get)
                .forEach(message -> MessageUtil.sendMessage(source, message, false));
    }

    // Component I18n cannot be processed with logger.
    public static void sendToConsole(String message) {
        MessageUtil.sendToConsoleCompat(ComponentCompat.literal(message));
    }

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

    public static void sendServerMessage(String message) {
        MessageUtil.sendServerMessageCompat(ComponentCompat.literal(message));
    }

    public static void sendServerMessageCompat(@NotNull ComponentCompat message) {
        MessageUtil.sendServerMessage(message.get());
    }

    public static void sendServerMessage(Component message) {
        MessageUtil.sendToConsole(message);
        MagicLibMinecraft.getInstance().getServer().ifPresent(server ->
                server.getPlayerList().getPlayers().forEach(player ->
                        MessageUtil.sendMessage(player.createCommandSourceStack(), message)));
    }
}
