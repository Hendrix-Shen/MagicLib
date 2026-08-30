package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.scores.DisplaySlot;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.ScoreboardSlotArgument}
 * for MC 1.20.2+.
 *
 * <p>Since MC 1.20.2 the vanilla argument returns a {@link DisplaySlot} enum instead of an
 * {@code int}. The parsing and suggestion logic is identical to the vanilla implementation.</p>
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.20.1: subproject 1.16.5 (main project)</li>
 * <li>mc1.20.2+        : subproject 1.20.2        &lt;--------</li>
 */
public class ClientScoreboardSlotArgument implements ArgumentType<DisplaySlot> {
    private static final Collection<String> EXAMPLES = Arrays.asList("sidebar", "foo.bar");
    private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("argument.scoreboardDisplaySlot.invalid", object));

    private ClientScoreboardSlotArgument() {
    }

    /**
     * Creates a scoreboard slot argument.
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
    public static DisplaySlot getDisplaySlot(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, DisplaySlot.class);
    }

    @Override
    public DisplaySlot parse(StringReader stringReader) throws CommandSyntaxException {
        String string = stringReader.readUnquotedString();
        DisplaySlot displaySlot = DisplaySlot.CODEC.byName(string);

        if (displaySlot == null) {
            throw ClientScoreboardSlotArgument.ERROR_INVALID_VALUE.createWithContext(stringReader, string);
        }

        return displaySlot;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(
                Arrays.stream(DisplaySlot.values()).map(DisplaySlot::getSerializedName), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return ClientScoreboardSlotArgument.EXAMPLES;
    }
}
