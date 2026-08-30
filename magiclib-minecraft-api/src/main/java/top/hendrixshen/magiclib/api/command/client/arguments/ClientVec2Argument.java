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
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.coordinates.Vec2Argument}.
 *
 * <p>The parsing and suggestion logic is identical to the vanilla implementation. Only the getter
 * methods use a {@link CommandContext} of {@link ClientCommandSource} instead of
 * {@code CommandSourceStack}.</p>
 */
public class ClientVec2Argument implements ArgumentType<ClientCoordinates> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0 0", "~ ~", "0.1 -0.5", "~1 ~-2");
    private static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.pos2d.incomplete"));
    private final boolean centerCorrect;

    private ClientVec2Argument(boolean centerCorrect) {
        this.centerCorrect = centerCorrect;
    }

    /**
     * Creates a vec2 argument.
     *
     * @return the argument
     */
    public static ClientVec2Argument vec2() {
        return new ClientVec2Argument(true);
    }

    /**
     * Gets the vec2 from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the vec2
     */
    public static Vec2 getVec2(CommandContext<ClientCommandSource> context, String name) {
        Vec3 position = context.getArgument(name, ClientCoordinates.class).getPosition(context.getSource());
        return new Vec2((float) position.x, (float) position.z);
    }

    @Override
    public ClientCoordinates parse(StringReader stringReader) throws CommandSyntaxException {
        int start = stringReader.getCursor();

        if (!stringReader.canRead()) {
            throw ClientVec2Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader);
        }

        WorldCoordinate x = WorldCoordinate.parseDouble(stringReader, this.centerCorrect);

        if (stringReader.canRead() && stringReader.peek() == ' ') {
            stringReader.skip();
            WorldCoordinate z = WorldCoordinate.parseDouble(stringReader, this.centerCorrect);
            return new ClientWorldCoordinates(x, new WorldCoordinate(true, 0.0), z);
        }

        stringReader.setCursor(start);
        throw ClientVec2Argument.ERROR_NOT_COMPLETE.createWithContext(stringReader);
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

        return SharedSuggestionProvider.suggest2DCoordinates(string, collection, builder, Commands.createValidator(this::parse));
    }

    @Override
    public Collection<String> getExamples() {
        return ClientVec2Argument.EXAMPLES;
    }
}
