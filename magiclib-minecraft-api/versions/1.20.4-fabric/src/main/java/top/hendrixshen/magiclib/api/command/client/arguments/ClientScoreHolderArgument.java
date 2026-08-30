package top.hendrixshen.magiclib.api.command.client.arguments;

import com.google.common.collect.Lists;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.ScoreHolder;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Supplier;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.ScoreHolderArgument}.
 *
 * <p>The parsing logic is identical to the vanilla implementation. Only the getter methods use a
 * {@link CommandContext} of {@link ClientCommandSource} instead of {@code CommandSourceStack},
 * resolving the score holders from the client level.</p>
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.20.2: subproject 1.16.5 (main project)</li>
 * <li>mc1.20.3+         : subproject 1.20.4        &lt;--------</li>
 */
public class ClientScoreHolderArgument implements ArgumentType<ClientScoreHolderArgument.Result> {
    /**
     * A suggestion provider for score holders.
     */
    public static final SuggestionProvider<ClientCommandSource> SUGGEST_SCORE_HOLDERS = (context, builder) -> {
        StringReader stringReader = new StringReader(builder.getInput());
        stringReader.setCursor(builder.getStart());
        ClientEntitySelectorParser parser = new ClientEntitySelectorParser(stringReader);

        try {
            parser.parse();
        } catch (CommandSyntaxException e) {
            // ignored
        }

        return parser.fillSuggestions(builder, suggestionsBuilder ->
                SharedSuggestionProvider.suggest(context.getSource().getClient().getConnection().getOnlinePlayers()
                        .stream().map(playerInfo -> {
                            //#if MC >= 1.21.10
                            //$$ return playerInfo.getProfile().name();
                            //#else
                            return playerInfo.getProfile().getName();
                            //#endif
                        }), suggestionsBuilder));
    };
    private static final Collection<String> EXAMPLES = Arrays.asList("Player", "0123", "*", "@e");
    private static final SimpleCommandExceptionType ERROR_NO_RESULTS = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.scoreHolder.empty"));
    private final boolean multiple;

    private ClientScoreHolderArgument(boolean multiple) {
        this.multiple = multiple;
    }

    /**
     * Gets a single score holder name from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the score holder name
     * @throws CommandSyntaxException if no score holder matches
     */
    public static ScoreHolder getName(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        return ClientScoreHolderArgument.getNames(context, name).iterator().next();
    }

    /**
     * Gets the score holder names from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the score holder names
     * @throws CommandSyntaxException if no score holder matches
     */
    public static Collection<ScoreHolder> getNames(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        return ClientScoreHolderArgument.getNames(context, name, Collections::emptyList);
    }

    /**
     * Gets the score holder names from the context, defaulting to the wildcard.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the score holder names
     * @throws CommandSyntaxException if no score holder matches
     */
    public static Collection<ScoreHolder> getNamesWithDefaultWildcard(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        return ClientScoreHolderArgument.getNames(context, name,
                () -> context.getSource().getLevel().getScoreboard().getTrackedPlayers());
    }

    /**
     * Gets the score holder names from the context.
     *
     * @param context  the command context
     * @param name     the argument name
     * @param supplier the wildcard supplier
     * @return the score holder names
     * @throws CommandSyntaxException if no score holder matches
     */
    public static Collection<ScoreHolder> getNames(
            CommandContext<ClientCommandSource> context,
            String name,
            Supplier<Collection<ScoreHolder>> supplier
    ) throws CommandSyntaxException {
        Collection<ScoreHolder> collection = context.<ClientScoreHolderArgument.Result>getArgument(name, ClientScoreHolderArgument.Result.class)
                .getNames(context.getSource(), supplier);

        if (collection.isEmpty()) {
            throw net.minecraft.commands.arguments.EntityArgument.NO_ENTITIES_FOUND.create();
        }

        return collection;
    }

    /**
     * Creates a single score holder argument.
     *
     * @return the argument
     */
    public static ClientScoreHolderArgument scoreHolder() {
        return new ClientScoreHolderArgument(false);
    }

    /**
     * Creates a multiple score holders argument.
     *
     * @return the argument
     */
    public static ClientScoreHolderArgument scoreHolders() {
        return new ClientScoreHolderArgument(true);
    }

    @Override
    public ClientScoreHolderArgument.Result parse(StringReader stringReader) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '@') {
            ClientEntitySelectorParser parser = new ClientEntitySelectorParser(stringReader);
            ClientEntitySelector selector = parser.parse();

            if (!this.multiple && selector.getMaxResults() > 1) {
                throw net.minecraft.commands.arguments.EntityArgument.ERROR_NOT_SINGLE_ENTITY.createWithContext(stringReader);
            }

            return new ClientScoreHolderArgument.SelectorResult(selector);
        }

        int cursor = stringReader.getCursor();

        while (stringReader.canRead() && stringReader.peek() != ' ') {
            stringReader.skip();
        }

        String string = stringReader.getString().substring(cursor, stringReader.getCursor());

        if (string.equals("*")) {
            return (source, supplier) -> {
                Collection<ScoreHolder> collection = supplier.get();

                if (collection.isEmpty()) {
                    throw ClientScoreHolderArgument.ERROR_NO_RESULTS.create();
                }

                return collection;
            };
        }

        List<ScoreHolder> list = List.of(ScoreHolder.forNameOnly(string));

        if (string.startsWith("#")) {
            return (source, supplier) -> list;
        }

        return (source, supplier) -> list;
    }

    @Override
    public Collection<String> getExamples() {
        return ClientScoreHolderArgument.EXAMPLES;
    }

    /**
     * The result of parsing a score holder argument.
     */
    @FunctionalInterface
    public interface Result {
        /**
         * Resolves the score holder names.
         *
         * @param source   the client command source
         * @param supplier the wildcard supplier
         * @return the score holder names
         * @throws CommandSyntaxException if no score holder matches
         */
        Collection<ScoreHolder> getNames(ClientCommandSource source, Supplier<Collection<ScoreHolder>> supplier) throws CommandSyntaxException;
    }

    static class SelectorResult implements ClientScoreHolderArgument.Result {
        private final ClientEntitySelector selector;

        SelectorResult(ClientEntitySelector selector) {
            this.selector = selector;
        }

        @Override
        public Collection<ScoreHolder> getNames(ClientCommandSource source, Supplier<Collection<ScoreHolder>> supplier) throws CommandSyntaxException {
            List<? extends Entity> list = this.selector.findEntities(source);

            if (list.isEmpty()) {
                throw net.minecraft.commands.arguments.EntityArgument.NO_ENTITIES_FOUND.create();
            }

            return Lists.newArrayList(list);
        }
    }
}
