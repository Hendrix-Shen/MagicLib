package top.hendrixshen.magiclib.api.command.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;

import top.hendrixshen.magiclib.impl.command.client.ClientCommandRegistry;

import java.util.function.Consumer;

/**
 * The entry point for registering client-side commands.
 *
 * <p>Client-side commands are executed entirely on the client thread, so they
 * work in both singleplayer and multiplayer without being sent to the server.</p>
 *
 * <p>
 * Example:
 * <pre>
 * {@code
 * ClientCommandManager.register(dispatcher -> dispatcher.register(
 *         ClientCommandManager.literal("example")
 *                 .executes(context -> {
 *                     ClientCommandSource source = context.getSource();
 *                     source.sendSuccess(new TextComponent("Hello, world!"));
 *                     return 1;
 *                 })
 * ));
 * }
 * </pre>
 * </p>
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientCommandManager {
    /**
     * Registers client-side commands through the given consumer.
     *
     * @param consumer the registration callback that receives the dispatcher
     */
    public static void register(Consumer<CommandDispatcher<MagicCommandSource>> consumer) {
        ClientCommandRegistry.getInstance().register(consumer);
    }

    /**
     * Creates a literal argument builder.
     *
     * @param name the literal name
     * @return the created builder
     */
    public static LiteralArgumentBuilder<MagicCommandSource> literal(String name) {
        return LiteralArgumentBuilder.literal(name);
    }

    /**
     * Creates a required argument builder.
     *
     * @param name the name of the argument
     * @param type the type of the argument
     * @param <T>  the type of the parsed argument value
     * @return the created builder
     */
    public static <T> RequiredArgumentBuilder<MagicCommandSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }
}
