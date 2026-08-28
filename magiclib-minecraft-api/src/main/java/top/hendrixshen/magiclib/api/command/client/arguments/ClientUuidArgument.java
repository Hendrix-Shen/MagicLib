package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.UUID;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.UuidArgument}.
 *
 * <p>The parsing logic is delegated to the vanilla implementation, since it does not depend on the
 * command source. Only the getter method uses a {@link CommandContext} of
 * {@link ClientCommandSource} instead of {@code CommandSourceStack}.</p>
 *
 * <li>mc1.14 ~ mc1.15: subproject 1.15.2</li>
 * <li>mc1.16+        : subproject 1.16.5 (main project)        &lt;--------</li>
 */
public class ClientUuidArgument implements ArgumentType<UUID> {
    private static final Collection<String> EXAMPLES = Arrays.asList("dd12be42-52a9-4a91-a8a1-11c01849e498");

    private ClientUuidArgument() {
    }

    /**
     * Creates a UUID argument.
     *
     * @return the argument
     */
    public static ClientUuidArgument uuid() {
        return new ClientUuidArgument();
    }

    /**
     * Gets the UUID from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the UUID
     */
    public static UUID getUuid(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, UUID.class);
    }

    @Override
    public UUID parse(StringReader stringReader) throws CommandSyntaxException {
        return net.minecraft.commands.arguments.UuidArgument.uuid().parse(stringReader);
    }

    @Override
    public Collection<String> getExamples() {
        return ClientUuidArgument.EXAMPLES;
    }
}
