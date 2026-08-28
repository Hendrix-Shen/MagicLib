package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.blocks.BlockPredicateArgument;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.blocks.BlockPredicateArgument}
 * for MC 1.19.2+.
 *
 * <p>Since MC 1.19.2 the vanilla {@link BlockPredicateArgument} parses without any command source
 * dependency (only a {@link CommandBuildContext}) and its getter returns a plain
 * {@link Predicate}, it can be reused almost as-is for client-side commands.</p>
 *
 * <li>mc1.14 ~ mc1.18: subproject 1.16.5 (main project)</li>
 * <li>mc1.19+        : subproject 1.19.2        &lt;--------</li>
 */
public class ClientBlockPredicateArgument implements ArgumentType<BlockPredicateArgument.Result> {
    private static final Collection<String> EXAMPLES = Arrays.asList("stone", "minecraft:stone", "stone[foo=bar]", "#stone", "#stone[foo=bar]{baz=nbt}");
    private final BlockPredicateArgument delegate;

    private ClientBlockPredicateArgument(BlockPredicateArgument delegate) {
        this.delegate = delegate;
    }

    /**
     * Creates a block predicate argument.
     *
     * @param buildContext the command build context
     * @return the argument
     */
    public static ClientBlockPredicateArgument blockPredicate(CommandBuildContext buildContext) {
        return new ClientBlockPredicateArgument(BlockPredicateArgument.blockPredicate(buildContext));
    }

    /**
     * Gets the block predicate from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the block predicate
     */
    public static Predicate<BlockInWorld> getBlockPredicate(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, BlockPredicateArgument.Result.class);
    }

    @Override
    public BlockPredicateArgument.Result parse(StringReader stringReader) throws CommandSyntaxException {
        return this.delegate.parse(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return this.delegate.listSuggestions(context, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return ClientBlockPredicateArgument.EXAMPLES;
    }
}
