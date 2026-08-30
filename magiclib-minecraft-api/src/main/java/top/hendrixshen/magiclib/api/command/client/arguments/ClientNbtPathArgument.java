package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.arguments.NbtPathArgument;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.NbtPathArgument}.
 *
 * <p>The parsing logic is identical to the vanilla implementation, and is directly delegated to the
 * vanilla {@link NbtPathArgument}, since its parsing is pure and does not depend on the command
 * source. Only the getter method uses a {@link CommandContext} of {@link ClientCommandSource}
 * instead of {@code CommandSourceStack}.</p>
 */
public class ClientNbtPathArgument implements ArgumentType<NbtPathArgument.NbtPath> {
    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo.bar", "foo[0]", "[0]", "[]", "{foo=bar}");
    public static final SimpleCommandExceptionType ERROR_INVALID_NODE = new SimpleCommandExceptionType(
            ComponentCompat.translatable("arguments.nbtpath.node.invalid"));
    public static final DynamicCommandExceptionType ERROR_NOTHING_FOUND = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("arguments.nbtpath.nothing_found", object));

    private ClientNbtPathArgument() {
    }

    /**
     * Creates an nbt path argument.
     *
     * @return the argument
     */
    public static ClientNbtPathArgument nbtPath() {
        return new ClientNbtPathArgument();
    }

    /**
     * Gets the nbt path from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the nbt path
     */
    public static NbtPathArgument.NbtPath getPath(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, NbtPathArgument.NbtPath.class);
    }

    /**
     * Parses an nbt path from the reader.
     *
     * <p>The parsing is delegated to the vanilla {@link NbtPathArgument}, as it is a pure parsing
     * operation that does not require the command source.</p>
     *
     * @param stringReader the reader
     * @return the parsed nbt path
     * @throws CommandSyntaxException if the nbt path is invalid
     */
    @Override
    public NbtPathArgument.NbtPath parse(StringReader stringReader) throws CommandSyntaxException {
        return NbtPathArgument.nbtPath().parse(stringReader);
    }

    /**
     * Gets the examples of this argument.
     *
     * @return the examples
     */
    @Override
    public Collection<String> getExamples() {
        return ClientNbtPathArgument.EXAMPLES;
    }
}
