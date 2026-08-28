package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.entity.Entity;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.EntityArgument}.
 *
 * <p>The parsing logic is identical to the vanilla implementation. Only the getter methods use a
 * {@link CommandContext} of {@link ClientCommandSource} instead of {@code CommandSourceStack}, and
 * resolve entities against the client level.</p>
 */
public class ClientEntityArgument implements ArgumentType<ClientEntitySelector> {
    private static final Collection<String> EXAMPLES = Arrays.asList("Player", "0123", "@e", "@e[type=foo]", "dd12be42-52a9-4a91-a8a1-11c01849e498");
    private static final SimpleCommandExceptionType ERROR_NOT_SINGLE_ENTITY = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.entity.toomany"));
    private static final SimpleCommandExceptionType ERROR_NOT_SINGLE_PLAYER = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.player.toomany"));
    private static final SimpleCommandExceptionType NO_ENTITIES_FOUND = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.entity.notfound.entity"));
    private static final SimpleCommandExceptionType NO_PLAYERS_FOUND = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.entity.notfound.player"));
    private static final SimpleCommandExceptionType ERROR_ONLY_PLAYERS_ALLOWED = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.player.entities"));

    private final boolean single;
    private final boolean playersOnly;

    private ClientEntityArgument(boolean single, boolean playersOnly) {
        this.single = single;
        this.playersOnly = playersOnly;
    }

    /**
     * Creates an entity argument that accepts a single entity.
     *
     * @return the argument
     */
    public static ClientEntityArgument entity() {
        return new ClientEntityArgument(true, false);
    }

    /**
     * Creates an entity argument that accepts multiple entities.
     *
     * @return the argument
     */
    public static ClientEntityArgument entities() {
        return new ClientEntityArgument(false, false);
    }

    /**
     * Creates an entity argument that accepts a single player.
     *
     * @return the argument
     */
    public static ClientEntityArgument player() {
        return new ClientEntityArgument(true, true);
    }

    /**
     * Creates an entity argument that accepts multiple players.
     *
     * @return the argument
     */
    public static ClientEntityArgument players() {
        return new ClientEntityArgument(false, true);
    }

    /**
     * Gets a single entity from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the entity
     * @throws CommandSyntaxException if no or multiple entities match
     */
    public static Entity getEntity(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        ClientEntitySelector selector = context.getArgument(name, ClientEntitySelector.class);
        Entity entity = selector.findSingleEntity(context.getSource());

        if (entity == null) {
            throw ClientEntityArgument.NO_ENTITIES_FOUND.create();
        }

        return entity;
    }

    /**
     * Gets all entities from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the entities
     * @throws CommandSyntaxException if no entities match
     */
    public static Collection<? extends Entity> getEntities(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        List<? extends Entity> list = context.getArgument(name, ClientEntitySelector.class).findEntities(context.getSource());

        if (list.isEmpty()) {
            throw ClientEntityArgument.NO_ENTITIES_FOUND.create();
        }

        return list;
    }

    /**
     * Gets a single player from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the player
     * @throws CommandSyntaxException if no or multiple players match
     */
    public static AbstractClientPlayer getPlayer(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        ClientEntitySelector selector = context.getArgument(name, ClientEntitySelector.class);
        AbstractClientPlayer player = selector.findSinglePlayer(context.getSource());

        if (player == null) {
            throw ClientEntityArgument.NO_PLAYERS_FOUND.create();
        }

        return player;
    }

    /**
     * Gets all players from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the players
     * @throws CommandSyntaxException if no players match
     */
    public static Collection<AbstractClientPlayer> getPlayers(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        List<AbstractClientPlayer> list = context.getArgument(name, ClientEntitySelector.class).findPlayers(context.getSource());

        if (list.isEmpty()) {
            throw ClientEntityArgument.NO_PLAYERS_FOUND.create();
        }

        return list;
    }

    @Override
    public ClientEntitySelector parse(StringReader stringReader) throws CommandSyntaxException {
        int cursor = 0;
        ClientEntitySelectorParser parser = new ClientEntitySelectorParser(stringReader);
        ClientEntitySelector selector = parser.parse();

        if (selector.getMaxResults() > 1 && this.single) {
            if (this.playersOnly) {
                stringReader.setCursor(cursor);
                throw ClientEntityArgument.ERROR_NOT_SINGLE_PLAYER.createWithContext(stringReader);
            }

            stringReader.setCursor(cursor);
            throw ClientEntityArgument.ERROR_NOT_SINGLE_ENTITY.createWithContext(stringReader);
        }

        if (selector.includesEntities() && this.playersOnly && !selector.isSelfSelector()) {
            stringReader.setCursor(cursor);
            throw ClientEntityArgument.ERROR_ONLY_PLAYERS_ALLOWED.createWithContext(stringReader);
        }

        return selector;
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof SharedSuggestionProvider) {
            StringReader stringReader = new StringReader(builder.getInput());
            stringReader.setCursor(builder.getStart());
            ClientEntitySelectorParser parser = new ClientEntitySelectorParser(stringReader, true);

            try {
                parser.parse();
            } catch (CommandSyntaxException e) {
                // ignored
            }

            return parser.fillSuggestions(builder, suggestionsBuilder -> {
                Collection<String> collection = ((SharedSuggestionProvider) context.getSource()).getOnlinePlayerNames();

                if (!this.playersOnly) {
                    collection = com.google.common.collect.Lists.newArrayList(com.google.common.collect.Iterables.concat(
                            collection, ((SharedSuggestionProvider) context.getSource()).getSelectedEntities()));
                }

                SharedSuggestionProvider.suggest(collection, suggestionsBuilder);
            });
        }

        return Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return ClientEntityArgument.EXAMPLES;
    }
}
