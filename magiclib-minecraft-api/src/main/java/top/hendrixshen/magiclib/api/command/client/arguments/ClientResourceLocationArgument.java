package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.ResourceLocationArgument;
import net.minecraft.resources.ResourceLocation;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Arrays;
import java.util.Collection;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.ResourceLocationArgument}.
 *
 * <p>The parsing logic is delegated to the vanilla implementation, as it only performs pure parsing and does
 * not depend on the command source. Only the getter method uses a {@link CommandContext} of
 * {@link ClientCommandSource} instead of {@code CommandSourceStack}.</p>
 */
public class ClientResourceLocationArgument implements ArgumentType<ResourceLocation> {
    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "012");

    private ClientResourceLocationArgument() {
    }

    /**
     * Creates a resource location argument.
     *
     * @return the argument
     */
    public static ClientResourceLocationArgument id() {
        return new ClientResourceLocationArgument();
    }

    /**
     * Gets the resource location from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the resource location
     */
    public static ResourceLocation getId(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, ResourceLocation.class);
    }

    /**
     * Parses a resource location from the reader, delegating to the vanilla implementation.
     *
     * @param stringReader the string reader
     * @return the parsed resource location
     * @throws CommandSyntaxException if the resource location is invalid
     */
    @Override
    public ResourceLocation parse(StringReader stringReader) throws CommandSyntaxException {
        return ResourceLocationArgument.id().parse(stringReader);
    }

    /**
     * Gets the example strings for this argument.
     *
     * @return the example strings
     */
    @Override
    public Collection<String> getExamples() {
        return ClientResourceLocationArgument.EXAMPLES;
    }
}
