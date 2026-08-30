package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.scores.TeamColor;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.TeamColorArgument}
 * for MC 26.2+.
 *
 * <p>Since MC 26.2 the vanilla color argument returns a {@link TeamColor} instead of a
 * {@code ChatFormatting}. The parsing and suggestion logic is identical to the vanilla
 * implementation.</p>
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc26.1: subproject 1.16.5 (main project)</li>
 * <li>mc26.2+        : subproject 26.2        &lt;--------</li>
 */
public class ClientColorArgument implements ArgumentType<TeamColor> {
    private static final Collection<String> EXAMPLES = Arrays.asList("red", "green");
    private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("argument.color.invalid", object));

    private ClientColorArgument() {
    }

    /**
     * Creates a color argument.
     *
     * @return the argument
     */
    public static ClientColorArgument color() {
        return new ClientColorArgument();
    }

    /**
     * Gets the team color from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the team color
     */
    public static TeamColor getColor(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, TeamColor.class);
    }

    @Override
    public TeamColor parse(StringReader stringReader) throws CommandSyntaxException {
        String string = stringReader.readUnquotedString();
        TeamColor teamColor = TeamColor.byName(string);

        if (teamColor == null) {
            throw ClientColorArgument.ERROR_INVALID_VALUE.create(string);
        }

        return teamColor;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(TeamColor.VALUES.stream().map(TeamColor::getSerializedName), builder);
    }

    @Override
    public Collection<String> getExamples() {
        return ClientColorArgument.EXAMPLES;
    }
}
