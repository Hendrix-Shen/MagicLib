package top.hendrixshen.magiclib.api.command.client.arguments;

import com.google.common.collect.Lists;

import com.mojang.authlib.GameProfile;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.commands.SharedSuggestionProvider;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.GameProfileArgument}.
 *
 * <p>The parsing logic is identical to the vanilla implementation. Only the getter methods use a
 * {@link CommandContext} of {@link ClientCommandSource} instead of {@code CommandSourceStack},
 * resolving the profiles from the client connection.</p>
 */
public class ClientGameProfileArgument implements ArgumentType<ClientGameProfileArgument.Result> {
    private static final Collection<String> EXAMPLES = Arrays.asList("Player", "0123", "dd12be42-52a9-4a91-a8a1-11c01849e498", "@e");
    private static final SimpleCommandExceptionType ERROR_UNKNOWN_PLAYER = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.player.unknown"));

    private ClientGameProfileArgument() {
    }

    /**
     * Creates a game profile argument.
     *
     * @return the argument
     */
    public static ClientGameProfileArgument gameProfile() {
        return new ClientGameProfileArgument();
    }

    /**
     * Gets the game profiles from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the game profiles
     * @throws CommandSyntaxException if the player is unknown
     */
    public static Collection<GameProfile> getGameProfiles(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        return context.<ClientGameProfileArgument.Result>getArgument(name, ClientGameProfileArgument.Result.class)
                .getNames(context.getSource());
    }

    @Override
    public ClientGameProfileArgument.Result parse(StringReader stringReader) throws CommandSyntaxException {
        if (stringReader.canRead() && stringReader.peek() == '@') {
            ClientEntitySelectorParser parser = new ClientEntitySelectorParser(stringReader);
            ClientEntitySelector selector = parser.parse();

            if (selector.includesEntities()) {
                throw net.minecraft.commands.arguments.EntityArgument.ERROR_ONLY_PLAYERS_ALLOWED.create();
            }

            return new ClientGameProfileArgument.SelectorResult(selector);
        }

        int cursor = stringReader.getCursor();

        while (stringReader.canRead() && stringReader.peek() != ' ') {
            stringReader.skip();
        }

        String string = stringReader.getString().substring(cursor, stringReader.getCursor());
        return source -> {
            for (PlayerInfo playerInfo : source.getClient().getConnection().getOnlinePlayers()) {
                if (
                        //#if MC >= 1.21.10
                        //$$ playerInfo.getProfile().name().equals(string)
                        //#else
                        playerInfo.getProfile().getName().equals(string)
                        //#endif
                ) {
                    return Collections.singleton(playerInfo.getProfile());
                }
            }

            throw ClientGameProfileArgument.ERROR_UNKNOWN_PLAYER.create();
        };
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (context.getSource() instanceof SharedSuggestionProvider) {
            StringReader stringReader = new StringReader(builder.getInput());
            stringReader.setCursor(builder.getStart());
            ClientEntitySelectorParser parser = new ClientEntitySelectorParser(stringReader);

            try {
                parser.parse();
            } catch (CommandSyntaxException e) {
                // ignored
            }

            return parser.fillSuggestions(builder, suggestionsBuilder ->
                    SharedSuggestionProvider.suggest(
                            ((SharedSuggestionProvider) context.getSource()).getOnlinePlayerNames(), suggestionsBuilder));
        }

        return Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return ClientGameProfileArgument.EXAMPLES;
    }

    /**
     * The result of parsing a game profile argument.
     */
    @FunctionalInterface
    public interface Result {
        /**
         * Resolves the game profiles.
         *
         * @param source the client command source
         * @return the game profiles
         * @throws CommandSyntaxException if the player is unknown
         */
        Collection<GameProfile> getNames(ClientCommandSource source) throws CommandSyntaxException;
    }

    static class SelectorResult implements ClientGameProfileArgument.Result {
        private final ClientEntitySelector selector;

        SelectorResult(ClientEntitySelector selector) {
            this.selector = selector;
        }

        @Override
        public Collection<GameProfile> getNames(ClientCommandSource source) throws CommandSyntaxException {
            List<? extends AbstractClientPlayer> list = this.selector.findPlayers(source);

            if (list.isEmpty()) {
                throw net.minecraft.commands.arguments.EntityArgument.NO_PLAYERS_FOUND.create();
            }

            List<GameProfile> list2 = Lists.newArrayList();

            for (AbstractClientPlayer player : list) {
                list2.add(player.getGameProfile());
            }

            return list2;
        }
    }
}
