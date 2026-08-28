package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.scores.Objective;
import net.minecraft.world.scores.Scoreboard;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.ObjectiveArgument}.
 *
 * <p>The parsing logic is identical to the vanilla implementation. Only the getter methods use a
 * {@link CommandContext} of {@link ClientCommandSource} instead of {@code CommandSourceStack},
 * resolving the objective from the client scoreboard.</p>
 */
public class ClientObjectiveArgument implements ArgumentType<String> {
    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "*", "012");
    private static final DynamicCommandExceptionType ERROR_OBJECTIVE_NOT_FOUND = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("arguments.objective.notFound", object));
    private static final DynamicCommandExceptionType ERROR_OBJECTIVE_READ_ONLY = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("arguments.objective.readonly", object));
    private static final DynamicCommandExceptionType ERROR_OBJECTIVE_NAME_TOO_LONG = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("commands.scoreboard.objectives.add.longName", object));

    private ClientObjectiveArgument() {
    }

    /**
     * Creates an objective argument.
     *
     * @return the argument
     */
    public static ClientObjectiveArgument objective() {
        return new ClientObjectiveArgument();
    }

    /**
     * Gets the objective from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the objective
     * @throws CommandSyntaxException if the objective does not exist
     */
    public static Objective getObjective(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        String string = context.getArgument(name, String.class);
        Scoreboard scoreboard = context.getSource().getLevel().getScoreboard();
        Objective objective = scoreboard.getObjective(string);

        if (objective == null) {
            throw ClientObjectiveArgument.ERROR_OBJECTIVE_NOT_FOUND.create(string);
        }

        return objective;
    }

    /**
     * Gets a writable objective from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the objective
     * @throws CommandSyntaxException if the objective does not exist or is read-only
     */
    public static Objective getWritableObjective(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        Objective objective = ClientObjectiveArgument.getObjective(context, name);

        if (objective.getCriteria().isReadOnly()) {
            throw ClientObjectiveArgument.ERROR_OBJECTIVE_READ_ONLY.create(objective.getName());
        }

        return objective;
    }

    @Override
    public String parse(StringReader stringReader) throws CommandSyntaxException {
        String string = stringReader.readUnquotedString();

        if (string.length() > 16) {
            throw ClientObjectiveArgument.ERROR_OBJECTIVE_NAME_TOO_LONG.create(16);
        }

        return string;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof ClientCommandSource) {
            Scoreboard scoreboard = ((ClientCommandSource) context.getSource()).getLevel().getScoreboard();
            return SharedSuggestionProvider.suggest(scoreboard.getObjectiveNames(), builder);
        }

        return Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return ClientObjectiveArgument.EXAMPLES;
    }
}
