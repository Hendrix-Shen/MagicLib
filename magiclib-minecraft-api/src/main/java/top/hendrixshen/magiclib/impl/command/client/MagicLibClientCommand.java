package top.hendrixshen.magiclib.impl.command.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;

import top.hendrixshen.magiclib.SharedConstants;
import top.hendrixshen.magiclib.api.command.client.ClientCommandManager;
import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The built-in {@code /magiclib:client} command.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MagicLibClientCommand {
    private static final String COMMAND_NAME = "magiclib:client";
    private static final String DUMP_FILE = "magiclib-client-command.txt";

    /**
     * Registers the built-in client command.
     */
    public static void init() {
        ClientCommandManager.register(dispatcher -> dispatcher.register(
                ClientCommandManager.literal(MagicLibClientCommand.COMMAND_NAME)
                        .then(ClientCommandManager.literal("version")
                                .executes(MagicLibClientCommand::executeVersion))
                        .then(ClientCommandManager.literal("dump")
                                .executes(MagicLibClientCommand::executeDump))
        ));
    }

    private static int executeVersion(CommandContext<ClientCommandSource> context) {
        ClientCommandSource source = context.getSource();
        source.sendSuccess(ComponentCompat.translatable(
                "magiclib.command.client.version",
                SharedConstants.getMagiclibName(),
                SharedConstants.getMagiclibVersion(),
                SharedConstants.getMagiclibVersionType()
        ));
        return 1;
    }

    private static int executeDump(CommandContext<ClientCommandSource> context) {
        ClientCommandSource source = context.getSource();
        CommandDispatcher<ClientCommandSource> dispatcher = ClientCommandRegistry.getInstance().getDispatcher();
        StringBuilder builder = new StringBuilder();

        for (CommandNode<ClientCommandSource> child : dispatcher.getRoot().getChildren()) {
            MagicLibClientCommand.collectCommands(child, "/", builder);
        }

        Path path = Paths.get(MagicLibClientCommand.DUMP_FILE);

        try {
            Files.write(path, builder.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            source.sendFailure(ComponentCompat.translatable(
                    "magiclib.command.client.dump.fail", path.toAbsolutePath().normalize()));
            return 0;
        }

        source.sendSuccess(ComponentCompat.translatable(
                "magiclib.command.client.dump.success", path.toAbsolutePath().normalize()));
        return 1;
    }

    private static void collectCommands(CommandNode<ClientCommandSource> node, String prefix, StringBuilder builder) {
        String path = prefix + node.getName();

        if (node.getCommand() != null) {
            builder.append(path).append(System.lineSeparator());
        }

        List<CommandNode<ClientCommandSource>> children = node.getChildren().stream()
                .sorted((a, b) -> a.getName().compareTo(b.getName()))
                .collect(Collectors.toList());

        for (CommandNode<ClientCommandSource> child : children) {
            MagicLibClientCommand.collectCommands(child, path + " ", builder);
        }
    }
}
