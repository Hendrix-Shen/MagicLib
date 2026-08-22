package top.hendrixshen.magiclib.impl.command.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.exceptions.BuiltInExceptionProvider;
import com.mojang.brigadier.exceptions.CommandExceptionType;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentUtils;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.command.client.MagicCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.HashMap;
import java.util.Map;

/**
 * The internal implementation of the client-side command system.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientCommandInternals {
    private static final char COMMAND_PREFIX = '/';

    /**
     * Executes a client-side command from a chat message.
     *
     * <p>The message is expected to keep the command prefix, e.g. {@code /example}.</p>
     *
     * @param message the chat message
     * @return true if the message was handled by the client-side command system,
     * and should not be sent to the server
     */
    public static boolean executeChatMessage(String message) {
        if (!ClientCommandInternals.isCommandMessage(message)) {
            return false;
        }

        return ClientCommandInternals.executeCommand(message.substring(1));
    }

    /**
     * Executes a client-side command.
     *
     * <p>The command is expected to have the command prefix stripped, e.g. {@code example}.</p>
     *
     * @param command the command
     * @return true if the command was handled by the client-side command system,
     * and should not be sent to the server
     */
    public static boolean executeCommand(String command) {
        if (command.isEmpty()) {
            return false;
        }

        Minecraft client = Minecraft.getInstance();
        //#if MC >= 26.2
        //$$ ChatComponent chat = client.gui.hud.getChat();
        //#else
        ChatComponent chat = client.gui.getChat();
        //#endif
        MagicCommandSource source = new MagicCommandSourceImpl(client);
        chat.addRecentChat(ClientCommandInternals.COMMAND_PREFIX + command);

        try {
            ClientCommandRegistry.getInstance().getDispatcher().execute(command, source);
            return true;
        } catch (CommandSyntaxException e) {
            return ClientCommandInternals.handleSyntaxError(e, source);
        } catch (RuntimeException e) {
            MagicLib.getLogger().warn("Error while executing client-sided command '{}'", command, e);
            source.sendFailure(ComponentCompat.literal(e.getMessage()));
            return true;
        } finally {
            chat.resetChatScroll();
        }
    }

    /**
     * Merges the registered client commands into the given dispatcher, so that
     * the vanilla suggestion system can complete them.
     *
     * @param target the dispatcher to merge the client commands into
     */
    public static void addCommands(CommandDispatcher<MagicCommandSource> target) {
        MagicCommandSource source = new MagicCommandSourceImpl(Minecraft.getInstance());
        CommandDispatcher<MagicCommandSource> sourceDispatcher = ClientCommandRegistry.getInstance().getDispatcher();
        Map<CommandNode<MagicCommandSource>, CommandNode<MagicCommandSource>> nodeMapping = new HashMap<>();
        nodeMapping.put(sourceDispatcher.getRoot(), target.getRoot());
        ClientCommandInternals.copyTree(sourceDispatcher.getRoot(), target.getRoot(), source, nodeMapping);
    }

    /**
     * Copies the command tree of {@code origin} into {@code target}, keeping only
     * the commands usable by {@code source}. Redirects are remapped through
     * {@code nodeMapping}.
     *
     * @param origin      the origin command node
     * @param target      the target command node
     * @param source      the command source used to filter the commands
     * @param nodeMapping the mapping from original nodes to their copies,
     *                    used to resolve redirects
     */
    private static void copyTree(
            CommandNode<MagicCommandSource> origin,
            CommandNode<MagicCommandSource> target,
            MagicCommandSource source,
            Map<CommandNode<MagicCommandSource>, CommandNode<MagicCommandSource>> nodeMapping
    ) {
        for (CommandNode<MagicCommandSource> child : origin.getChildren()) {
            if (!child.canUse(source)) {
                continue;
            }

            CommandNode<MagicCommandSource> copy = ClientCommandInternals.copyNode(child, nodeMapping);
            nodeMapping.put(child, copy);
            target.addChild(copy);

            if (!child.getChildren().isEmpty()) {
                ClientCommandInternals.copyTree(child, copy, source, nodeMapping);
            }
        }
    }

    /**
     * Builds a copy of the given command node for suggestion purposes.
     *
     * <p>The copy drops the original requirements and executor, since the copy is
     * only used by the suggestion system. Redirects are remapped through
     * {@code nodeMapping}.</p>
     *
     * @param node        the command node to copy
     * @param nodeMapping the mapping from original nodes to their copies,
     *                    used to resolve redirects
     * @return the copied command node
     */
    private static CommandNode<MagicCommandSource> copyNode(
            CommandNode<MagicCommandSource> node,
            Map<CommandNode<MagicCommandSource>, CommandNode<MagicCommandSource>> nodeMapping
    ) {
        ArgumentBuilder<MagicCommandSource, ?> builder = node.createBuilder();
        builder.requires(s -> true);

        if (builder.getCommand() != null) {
            builder.executes(context -> 0);
        }

        if (builder.getRedirect() != null) {
            builder.redirect(nodeMapping.get(builder.getRedirect()));
        }

        return builder.build();
    }

    /**
     * Tests whether the message looks like a command input, i.e. it is not empty
     * and starts with the command prefix.
     *
     * @param message the chat message
     * @return true if the message is a command input
     */
    private static boolean isCommandMessage(String message) {
        return !message.isEmpty() && message.charAt(0) == ClientCommandInternals.COMMAND_PREFIX;
    }

    /**
     * Handles a command syntax exception. If the exception means that the command
     * is not a client-side command, the message falls back to the server.
     *
     * @param e      the syntax exception
     * @param source the client command source
     * @return true if the message was handled by the client-side command system
     */
    private static boolean handleSyntaxError(CommandSyntaxException e, MagicCommandSource source) {
        if (ClientCommandInternals.shouldFallbackToServer(e.getType())) {
            return false;
        }

        source.sendFailure(ClientCommandInternals.buildErrorMessage(e));
        return true;
    }

    /**
     * Tests whether a command syntax exception with the given type should be
     * ignored and the message sent to the server.
     *
     * <p>Only unknown command and node parse exceptions are ignored. The
     * argument-related dispatcher exceptions are not ignored, because they can
     * only happen when the user enters a correct command.</p>
     *
     * @param type the exception type
     * @return true if the message should fall back to the server
     */
    private static boolean shouldFallbackToServer(CommandExceptionType type) {
        BuiltInExceptionProvider builtins = CommandSyntaxException.BUILT_IN_EXCEPTIONS;
        return type == builtins.dispatcherUnknownCommand() || type == builtins.dispatcherParseException();
    }

    /**
     * Builds the error message for a command syntax exception.
     *
     * @param e the exception
     * @return the error message
     */
    private static Component buildErrorMessage(CommandSyntaxException e) {
        Component message = ComponentUtils.fromMessage(e.getRawMessage());
        String context = e.getContext();
        return context != null ? ComponentCompat.translatable("command.context.parse_error", message, context) : message;
    }
}
