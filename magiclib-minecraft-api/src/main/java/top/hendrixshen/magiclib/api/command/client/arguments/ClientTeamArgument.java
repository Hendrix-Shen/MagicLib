package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Scoreboard;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.TeamArgument}.
 *
 * <p>The parsing logic is identical to the vanilla implementation. Only the getter method uses a
 * {@link CommandContext} of {@link ClientCommandSource} instead of {@code CommandSourceStack},
 * resolving the team from the client scoreboard.</p>
 */
public class ClientTeamArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "123");
    private static final DynamicCommandExceptionType ERROR_TEAM_NOT_FOUND = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("team.notFound", object));

    private ClientTeamArgument() {
    }

    /**
     * Creates a team argument.
     *
     * @return the argument
     */
    public static ClientTeamArgument team() {
        return new ClientTeamArgument();
    }

    /**
     * Gets the team from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the team
     * @throws CommandSyntaxException if the team does not exist
     */
    public static PlayerTeam getTeam(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        String string = context.getArgument(name, String.class);
        Scoreboard scoreboard = context.getSource().getLevel().getScoreboard();
        PlayerTeam playerTeam = scoreboard.getPlayerTeam(string);

        if (playerTeam == null) {
            throw ClientTeamArgument.ERROR_TEAM_NOT_FOUND.create(string);
        }

        return playerTeam;
    }

    @Override
    public String parse(StringReader stringReader) throws CommandSyntaxException {
        return stringReader.readUnquotedString();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof SharedSuggestionProvider
                ? SharedSuggestionProvider.suggest(((SharedSuggestionProvider) context.getSource()).getAllTeams(), builder)
                : Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return ClientTeamArgument.EXAMPLES;
    }
}
