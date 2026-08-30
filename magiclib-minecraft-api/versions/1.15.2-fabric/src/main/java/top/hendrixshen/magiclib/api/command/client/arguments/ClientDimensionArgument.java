package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.resources.ResourceLocation;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@code net.minecraft.commands.arguments.DimensionArgument}.
 *
 * <p>Since this version range has no dimension registry key, the getter simply returns the
 * dimension {@link ResourceLocation} from the context, and no dimension suggestions are provided to
 * the client.</p>
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.15: subproject 1.15.2        &lt;--------</li>
 * <li>mc1.16+        : subproject 1.16.5 (main project)</li>
 */
public class ClientDimensionArgument implements ArgumentType<ResourceLocation> {
    private ClientDimensionArgument() {
    }

    /**
     * Creates a dimension argument.
     *
     * @return the argument
     */
    public static ClientDimensionArgument dimension() {
        return new ClientDimensionArgument();
    }

    /**
     * Gets the dimension id from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the dimension id
     */
    public static ResourceLocation getDimension(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, ResourceLocation.class);
    }

    @Override
    public ResourceLocation parse(StringReader stringReader) throws CommandSyntaxException {
        return ResourceLocation.read(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.asList("overworld", "the_nether", "the_end");
    }
}
