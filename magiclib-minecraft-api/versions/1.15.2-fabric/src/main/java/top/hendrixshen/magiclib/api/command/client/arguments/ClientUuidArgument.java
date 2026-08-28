package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The client-side counterpart of {@code net.minecraft.commands.arguments.UuidArgument}.
 *
 * <p>Since this version range has no vanilla {@code UuidArgument}, an equivalent local parser is
 * used. Only the getter method uses a {@link CommandContext} of {@link ClientCommandSource} instead
 * of {@code CommandSourceStack}.</p>
 *
 * <li>mc1.14 ~ mc1.15: subproject 1.15.2        &lt;--------</li>
 * <li>mc1.16+        : subproject 1.16.5 (main project)</li>
 */
public class ClientUuidArgument implements ArgumentType<UUID> {
    private static final SimpleCommandExceptionType ERROR_INVALID_UUID = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.uuid.invalid"));
    private static final Collection<String> EXAMPLES = Collections.singletonList("dd12be42-52a9-4a91-a8a1-11c01849e498");
    private static final Pattern ALLOWED_CHARACTERS = Pattern.compile("^([-A-Fa-f0-9]+)");

    private ClientUuidArgument() {
    }

    /**
     * Creates a UUID argument.
     *
     * @return the argument
     */
    public static ClientUuidArgument uuid() {
        return new ClientUuidArgument();
    }

    /**
     * Gets the UUID from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the UUID
     */
    public static UUID getUuid(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, UUID.class);
    }

    @Override
    public UUID parse(StringReader stringReader) throws CommandSyntaxException {
        return ClientUuidArgument.parseLocally(stringReader);
    }

    private static UUID parseLocally(StringReader stringReader) throws CommandSyntaxException {
        String input = stringReader.getRemaining();
        Matcher matcher = ClientUuidArgument.ALLOWED_CHARACTERS.matcher(input);

        if (matcher.find()) {
            String matched = matcher.group(1);

            try {
                UUID uuid = UUID.fromString(matched);
                stringReader.setCursor(stringReader.getCursor() + matched.length());
                return uuid;
            } catch (IllegalArgumentException e) {
                // fall through
            }
        }

        throw ClientUuidArgument.ERROR_INVALID_UUID.create();
    }

    @Override
    public Collection<String> getExamples() {
        return ClientUuidArgument.EXAMPLES;
    }
}
