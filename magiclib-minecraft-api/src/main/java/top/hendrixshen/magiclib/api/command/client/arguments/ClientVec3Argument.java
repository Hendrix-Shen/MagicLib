package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.world.phys.Vec3;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.coordinates.Vec3Argument}.
 *
 * <p>The parsing and suggestion logic is identical to the vanilla implementation. Only the getter
 * methods use a {@link CommandContext} of {@link ClientCommandSource} instead of
 * {@code CommandSourceStack}.</p>
 */
public class ClientVec3Argument implements ArgumentType<ClientCoordinates> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "0.1 -0.5 .9", "~0.5 ~1 ~-5");
    public static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.pos3d.incomplete"));
    public static final SimpleCommandExceptionType ERROR_MIXED_TYPE = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.pos.mixed"));
    private final boolean centerCorrect;

    private ClientVec3Argument(boolean centerCorrect) {
        this.centerCorrect = centerCorrect;
    }

    /**
     * Creates a vec3 argument.
     *
     * @return the argument
     */
    public static ClientVec3Argument vec3() {
        return new ClientVec3Argument(true);
    }

    /**
     * Creates a vec3 argument.
     *
     * @param centerCorrect whether integer coordinates should be centered
     * @return the argument
     */
    public static ClientVec3Argument vec3(boolean centerCorrect) {
        return new ClientVec3Argument(centerCorrect);
    }

    /**
     * Gets the vec3 from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the vec3
     */
    public static Vec3 getVec3(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, ClientCoordinates.class).getPosition(context.getSource());
    }

    /**
     * Gets the coordinates from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the coordinates
     */
    public static ClientCoordinates getCoordinates(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, ClientCoordinates.class);
    }

    @Override
    public ClientCoordinates parse(StringReader stringReader) throws CommandSyntaxException {
        return stringReader.canRead() && stringReader.peek() == '^'
                ? ClientLocalCoordinates.parse(stringReader)
                : ClientWorldCoordinates.parse(stringReader, this.centerCorrect);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (!(context.getSource() instanceof SharedSuggestionProvider)) {
            return Suggestions.empty();
        }

        String string = builder.getRemaining();
        Collection<SharedSuggestionProvider.TextCoordinates> collection;

        if (!string.isEmpty() && string.charAt(0) == '^') {
            collection = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
        } else {
            collection = ((SharedSuggestionProvider) context.getSource()).getAbsoluteCoordinates();
        }

        return SharedSuggestionProvider.suggestCoordinates(string, collection, builder, Commands.createValidator(this::parse));
    }

    @Override
    public Collection<String> getExamples() {
        return ClientVec3Argument.EXAMPLES;
    }
}
