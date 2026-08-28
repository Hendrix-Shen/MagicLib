package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.arguments.blocks.BlockInput;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.blocks.BlockStateArgument}.
 *
 * <p>The parsing and suggestion logic is delegated to the vanilla implementation, since it does not
 * depend on the command source. Only the getter method uses a {@link CommandContext} of
 * {@link ClientCommandSource} instead of {@code CommandSourceStack}.</p>
 *
 * <li>mc1.14 ~ mc1.18: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.19+        : subproject 1.19.2</li>
 */
public class ClientBlockStateArgument implements ArgumentType<BlockInput> {
    private static final Collection<String> EXAMPLES = Arrays.asList("stone", "minecraft:stone", "stone[foo=bar]", "foo{bar=baz}");

    private ClientBlockStateArgument() {
    }

    /**
     * Creates a block state argument.
     *
     * @return the argument
     */
    public static ClientBlockStateArgument block() {
        return new ClientBlockStateArgument();
    }

    /**
     * Gets the block input from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the block input
     */
    public static BlockInput getBlock(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, BlockInput.class);
    }

    @Override
    public BlockInput parse(StringReader stringReader) throws CommandSyntaxException {
        return net.minecraft.commands.arguments.blocks.BlockStateArgument.block().parse(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return net.minecraft.commands.arguments.blocks.BlockStateArgument.block().listSuggestions(context, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return ClientBlockStateArgument.EXAMPLES;
    }
}
