package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ScoreboardSlotArgument;
import net.minecraft.world.scores.Scoreboard;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.ScoreboardSlotArgument}.
 *
 * <p>The parsing logic is delegated to a vanilla {@link ScoreboardSlotArgument} instance, as it only
 * performs pure parsing and does not depend on the command source. Only the getter method uses a
 * {@link CommandContext} of {@link ClientCommandSource} instead of {@code CommandSourceStack}.</p>
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.20.1: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.20.2+         : subproject 1.20.2</li>
 */
public class ClientScoreboardSlotArgument implements ArgumentType<Integer> {
    private static final Collection<String> EXAMPLES = Arrays.asList("sidebar", "foo.bar");
    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("argument.scoreboardDisplaySlot.invalid", object));

    private ClientScoreboardSlotArgument() {
    }

    /**
     * Creates a display slot argument.
     *
     * @return the argument
     */
    public static ClientScoreboardSlotArgument displaySlot() {
        return new ClientScoreboardSlotArgument();
    }

    /**
     * Gets the display slot from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the display slot
     */
    public static int getDisplaySlot(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, Integer.class);
    }

    /**
     * Parses a display slot from the reader, delegating to the vanilla implementation.
     *
     * @param stringReader the string reader
     * @return the parsed display slot
     * @throws CommandSyntaxException if the display slot is invalid
     */
    @Override
    public Integer parse(StringReader stringReader) throws CommandSyntaxException {
        return ScoreboardSlotArgument.displaySlot().parse(stringReader);
    }

    /**
     * Suggests the display slot names.
     *
     * @param context            the command context
     * @param suggestionsBuilder the suggestions builder
     * @param <S>                the type of the command source
     * @return the suggestions for the current input
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder suggestionsBuilder) {
        return SharedSuggestionProvider.suggest(Scoreboard.getDisplaySlotNames(), suggestionsBuilder);
    }

    /**
     * Gets the example strings for this argument.
     *
     * @return the example strings
     */
    @Override
    public Collection<String> getExamples() {
        return ClientScoreboardSlotArgument.EXAMPLES;
    }
}
