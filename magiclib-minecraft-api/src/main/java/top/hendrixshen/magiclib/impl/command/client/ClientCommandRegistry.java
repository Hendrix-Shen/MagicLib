package top.hendrixshen.magiclib.impl.command.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import com.mojang.brigadier.CommandDispatcher;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.function.Consumer;

/**
 * The internal registry that holds the client-side command dispatcher.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClientCommandRegistry {
    private static final ClientCommandRegistry instance = new ClientCommandRegistry();

    private final CommandDispatcher<ClientCommandSource> dispatcher = new CommandDispatcher<>();

    /**
     * Gets the singleton instance of the registry.
     *
     * @return the registry
     */
    public static ClientCommandRegistry getInstance() {
        return ClientCommandRegistry.instance;
    }

    /**
     * Registers client-side commands through the given consumer.
     *
     * @param consumer the registration callback that receives the dispatcher
     */
    public void register(Consumer<CommandDispatcher<ClientCommandSource>> consumer) {
        consumer.accept(this.dispatcher);
    }

    /**
     * Gets the dispatcher that holds all registered client-side commands.
     *
     * @return the dispatcher
     */
    public CommandDispatcher<ClientCommandSource> getDispatcher() {
        return this.dispatcher;
    }
}
