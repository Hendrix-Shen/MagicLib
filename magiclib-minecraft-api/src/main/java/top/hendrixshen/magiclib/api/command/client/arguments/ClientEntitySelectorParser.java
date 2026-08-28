package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import java.util.function.Predicate;

/**
 * A minimal client-side entity selector parser.
 *
 * <p>Supports the vanilla selectors ({@code @s}, {@code @p}, {@code @a}, {@code @r},
 * {@code @e}) as well as plain player names and UUIDs. The {@code [type=...]},
 * {@code [limit=...]} and {@code [sort=...]} options are supported; other options are
 * accepted and ignored, since the client has no server-side data for them.</p>
 */
public class ClientEntitySelectorParser {
    private static final SimpleCommandExceptionType ERROR_INVALID_NAME_OR_UUID = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.entity.invalid"));
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_SELECTOR_TYPE = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("argument.entity.selector.unknown", object));
    private static final SimpleCommandExceptionType ERROR_MISSING_SELECTOR_TYPE = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.entity.selector.missing"));

    private final StringReader reader;
    private final boolean allowSelectors;
    private int maxResults = Integer.MAX_VALUE;
    private boolean includesEntities = true;
    private boolean currentEntity;
    private Predicate<Entity> predicate = entity -> true;
    private String playerName;
    private UUID entityUUID;

    /**
     * Creates a client entity selector parser.
     *
     * @param reader the reader
     */
    public ClientEntitySelectorParser(StringReader reader) {
        this(reader, true);
    }

    /**
     * Creates a client entity selector parser.
     *
     * @param reader         the reader
     * @param allowSelectors whether selector tokens (starting with {@code @}) are allowed
     */
    public ClientEntitySelectorParser(StringReader reader, boolean allowSelectors) {
        this.reader = reader;
        this.allowSelectors = allowSelectors;
    }

    /**
     * Parses the selector.
     *
     * @return the parsed selector
     * @throws CommandSyntaxException if the input is malformed
     */
    public ClientEntitySelector parse() throws CommandSyntaxException {
        if (this.reader.canRead() && this.reader.peek() == '@') {
            if (!this.allowSelectors) {
                throw new SimpleCommandExceptionType(
                        ComponentCompat.translatable("argument.entity.selector.not_allowed")).createWithContext(this.reader);
            }

            this.reader.skip();
            this.parseSelector();
        } else {
            this.parseNameOrUUID();
        }

        return new ClientEntitySelector(
                this.maxResults,
                this.includesEntities,
                this.currentEntity,
                this.predicate,
                this.playerName,
                this.entityUUID
        );
    }

    private void parseSelector() throws CommandSyntaxException {
        if (!this.reader.canRead()) {
            throw ClientEntitySelectorParser.ERROR_MISSING_SELECTOR_TYPE.createWithContext(this.reader);
        }

        int cursor = this.reader.getCursor();
        char c = this.reader.read();

        switch (c) {
            case 'p':
                this.maxResults = 1;
                this.includesEntities = false;
                this.predicate = entity -> entity instanceof net.minecraft.client.player.AbstractClientPlayer;
                break;
            case 'a':
                this.maxResults = Integer.MAX_VALUE;
                this.includesEntities = false;
                this.predicate = entity -> entity instanceof net.minecraft.client.player.AbstractClientPlayer;
                break;
            case 'r':
                this.maxResults = 1;
                this.includesEntities = false;
                this.predicate = entity -> entity instanceof net.minecraft.client.player.AbstractClientPlayer;
                break;
            case 's':
                this.maxResults = 1;
                this.includesEntities = true;
                this.currentEntity = true;
                break;
            case 'e':
                this.maxResults = Integer.MAX_VALUE;
                this.includesEntities = true;
                this.predicate = Entity::isAlive;
                break;
            default:
                this.reader.setCursor(cursor);
                throw ClientEntitySelectorParser.ERROR_UNKNOWN_SELECTOR_TYPE.createWithContext(this.reader, '@' + c);
        }

        if (this.reader.canRead() && this.reader.peek() == '[') {
            this.reader.skip();
            this.parseOptions();
        }
    }

    private void parseNameOrUUID() throws CommandSyntaxException {
        int cursor = this.reader.getCursor();
        String name = this.reader.readString();

        try {
            this.entityUUID = UUID.fromString(name);
            this.includesEntities = true;
        } catch (IllegalArgumentException e) {
            if (name.isEmpty() || name.length() > 16) {
                this.reader.setCursor(cursor);
                throw ClientEntitySelectorParser.ERROR_INVALID_NAME_OR_UUID.createWithContext(this.reader);
            }

            this.includesEntities = false;
            this.playerName = name;
        }

        this.maxResults = 1;
    }

    private void parseOptions() throws CommandSyntaxException {
        this.reader.skipWhitespace();

        while (this.reader.canRead() && this.reader.peek() != ']') {
            this.reader.skipWhitespace();
            String option = this.reader.readString();
            this.reader.skipWhitespace();

            if (!this.reader.canRead() || this.reader.peek() != '=') {
                throw new SimpleCommandExceptionType(
                        ComponentCompat.translatable("argument.entity.options.valueless", option)).createWithContext(this.reader);
            }

            this.reader.skip();
            this.reader.skipWhitespace();

            if (option.equals("type")) {
                this.parseTypeOption();
            } else if (option.equals("limit")) {
                this.maxResults = Math.max(1, this.reader.readInt());
            } else if (option.equals("sort") || option.equals("distance") || option.equals("x")
                    || option.equals("y") || option.equals("z") || option.equals("dx")
                    || option.equals("dy") || option.equals("dz") || option.equals("name")
                    || option.equals("tag") || option.equals("team") || option.equals("level")
                    || option.equals("gamemode") || option.equals("nbt") || option.equals("scores")
                    || option.equals("advancements") || option.equals("predicate")) {
                // The value is parsed and discarded, since the client has no server-side data
                // for these options. The value is consumed so that the input is valid.
                this.reader.skipWhitespace();
                this.skipOptionValue();
            } else {
                throw new DynamicCommandExceptionType(
                        object -> ComponentCompat.translatable("argument.entity.options.unknown", object)).createWithContext(this.reader, option);
            }

            this.reader.skipWhitespace();

            if (this.reader.canRead() && this.reader.peek() == ',') {
                this.reader.skip();
            }
        }

        if (this.reader.canRead()) {
            this.reader.skip();
        } else {
            throw new SimpleCommandExceptionType(
                    ComponentCompat.translatable("argument.entity.options.unterminated")).createWithContext(this.reader);
        }
    }

    private void parseTypeOption() throws CommandSyntaxException {
        boolean invert = this.shouldInvertValue();
        EntityType<?> type;

        if (this.isTag()) {
            this.reader.readUnquotedString();
            return;
        }

        int cursor = this.reader.getCursor();
        String name = this.reader.readUnquotedString();
        //#if MC >= 26.2
        //$$ type = net.minecraft.core.registries.BuiltInRegistries.ENTITY_TYPE.getOptional(
        //$$         net.minecraft.resources.Identifier.tryParse(name)).orElse(null);
        //#else
        type = net.minecraft.core.Registry.ENTITY_TYPE.getOptional(
                net.minecraft.resources.ResourceLocation.tryParse(name)).orElse(null);
        //#endif

        if (type == null) {
            this.reader.setCursor(cursor);
            throw new DynamicCommandExceptionType(
                    object -> ComponentCompat.translatable("argument.entity.options.type.invalid", object)).createWithContext(this.reader, name);
        }

        EntityType<?> finalType = type;
        boolean finalInvert = invert;
        this.predicate = this.predicate.and(entity -> entity.getType() == finalType != finalInvert);

        if (
                //#if MC >= 26.2
                //$$ type == net.minecraft.world.entity.EntityTypes.PLAYER
                //#else
                type == EntityType.PLAYER
                        //#endif
                        && !invert
        ) {
            this.includesEntities = false;
        }
    }

    private void skipOptionValue() throws CommandSyntaxException {
        // Consume the value: either a quoted string, a number, or a balanced {...} block.
        if (this.reader.canRead() && this.reader.peek() == '{') {
            this.reader.skip();
            int depth = 1;

            while (this.reader.canRead() && depth > 0) {
                char c = this.reader.read();

                if (c == '{') {
                    depth++;
                } else if (c == '}') {
                    depth--;
                }
            }

            return;
        }

        if (this.reader.canRead() && (this.reader.peek() == '"' || this.reader.peek() == '\'')) {
            this.reader.readString();
            return;
        }

        this.reader.readUnquotedString();
    }

    private boolean shouldInvertValue() {
        this.reader.skipWhitespace();

        if (this.reader.canRead() && this.reader.peek() == '!') {
            this.reader.skip();
            this.reader.skipWhitespace();
            return true;
        }

        return false;
    }

    private boolean isTag() {
        this.reader.skipWhitespace();

        if (this.reader.canRead() && this.reader.peek() == '#') {
            this.reader.skip();
            this.reader.skipWhitespace();
            return true;
        }

        return false;
    }

    /**
     * Gets the underlying reader.
     *
     * @return the reader
     */
    public StringReader getReader() {
        return this.reader;
    }

    /**
     * Adds a predicate to the selector.
     *
     * @param predicate the predicate to add
     */
    public void addPredicate(Predicate<Entity> predicate) {
        this.predicate = this.predicate.and(predicate);
    }

    /**
     * Whether this parser can be used with the given source.
     *
     * @param source the source
     * @return true if selectors are allowed
     */
    public static boolean allowSelectors(ClientCommandSource source) {
        return true;
    }

    private CompletableFuture<Suggestions> suggestNameOrSelector(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> consumer) {
        consumer.accept(builder);

        if (this.allowSelectors) {
            builder.suggest("@p", ComponentCompat.translatable("argument.entity.selector.nearestPlayer"));
            builder.suggest("@a", ComponentCompat.translatable("argument.entity.selector.allPlayers"));
            builder.suggest("@r", ComponentCompat.translatable("argument.entity.selector.randomPlayer"));
            builder.suggest("@s", ComponentCompat.translatable("argument.entity.selector.self"));
            builder.suggest("@e", ComponentCompat.translatable("argument.entity.selector.allEntities"));
        }

        return builder.buildFuture();
    }

    /**
     * Fills the suggestions for the current parser state.
     *
     * @param builder  the suggestions builder
     * @param consumer the consumer for name suggestions
     * @return the suggestions future
     */
    public CompletableFuture<Suggestions> fillSuggestions(SuggestionsBuilder builder, Consumer<SuggestionsBuilder> consumer) {
        return this.suggestNameOrSelector(builder.createOffset(this.reader.getCursor()), consumer);
    }

    /**
     * Gets a list of example selectors.
     *
     * @return the examples
     */
    public static List<String> getExamples() {
        return Arrays.asList("Player", "0123", "@e", "@e[type=foo]", "dd12be42-52a9-4a91-a8a1-11c01849e498");
    }
}
