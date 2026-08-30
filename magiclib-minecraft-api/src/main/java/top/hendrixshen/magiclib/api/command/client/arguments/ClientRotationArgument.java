package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.coordinates.RotationArgument}.
 *
 * <p>The parsing logic is identical to the vanilla implementation. Only the getter methods use a
 * {@link CommandContext} of {@link ClientCommandSource} instead of {@code CommandSourceStack}.</p>
 */
public class ClientRotationArgument implements ArgumentType<ClientCoordinates> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0 0", "~ ~", "~-5 ~5");
    private static final SimpleCommandExceptionType ERROR_INCOMPLETE = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.rotation.incomplete"));

    private ClientRotationArgument() {
    }

    /**
     * Creates a rotation argument.
     *
     * @return the argument
     */
    public static ClientRotationArgument rotation() {
        return new ClientRotationArgument();
    }

    /**
     * Gets the rotation coordinates from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the rotation coordinates
     */
    public static ClientCoordinates getRotation(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, ClientCoordinates.class);
    }

    @Override
    public ClientCoordinates parse(StringReader stringReader) throws CommandSyntaxException {
        int start = stringReader.getCursor();

        if (!stringReader.canRead()) {
            throw ClientRotationArgument.ERROR_INCOMPLETE.createWithContext(stringReader);
        }

        WorldCoordinate y = WorldCoordinate.parseDouble(stringReader, false);

        if (stringReader.canRead() && stringReader.peek() == ' ') {
            stringReader.skip();
            WorldCoordinate x = WorldCoordinate.parseDouble(stringReader, false);
            return new ClientWorldCoordinates(x, y, new WorldCoordinate(true, 0.0));
        }

        stringReader.setCursor(start);
        throw ClientRotationArgument.ERROR_INCOMPLETE.createWithContext(stringReader);
    }

    @Override
    public Collection<String> getExamples() {
        return ClientRotationArgument.EXAMPLES;
    }
}
