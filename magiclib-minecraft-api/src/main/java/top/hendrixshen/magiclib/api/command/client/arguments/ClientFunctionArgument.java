package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.resources.ResourceLocation;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Arrays;
import java.util.Collection;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.item.FunctionArgument}.
 *
 * <p>Functions are managed by the server, so the client cannot resolve them into
 * {@code CommandFunction} instances. This client-side version only parses the function or tag id
 * and exposes it as a {@link ResourceLocation}.</p>
 */
public class ClientFunctionArgument implements ArgumentType<ResourceLocation> {
    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "#foo");
    private final boolean isTag;

    private ClientFunctionArgument(boolean isTag) {
        this.isTag = isTag;
    }

    /**
     * Creates a function argument.
     *
     * @return the argument
     */
    public static ClientFunctionArgument functions() {
        return new ClientFunctionArgument(false);
    }

    /**
     * Whether the parsed id refers to a tag.
     *
     * @return true if the id is a tag
     */
    public boolean isTag() {
        return this.isTag;
    }

    /**
     * Gets the function id from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the function id
     */
    public static ResourceLocation getFunction(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, ResourceLocation.class);
    }

    @Override
    public ResourceLocation parse(StringReader stringReader) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '#') {
            stringReader.skip();
            return ResourceLocation.read(stringReader);
        }

        return ResourceLocation.read(stringReader);
    }

    @Override
    public Collection<String> getExamples() {
        return ClientFunctionArgument.EXAMPLES;
    }
}
