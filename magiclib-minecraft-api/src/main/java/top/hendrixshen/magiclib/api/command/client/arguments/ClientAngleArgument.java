package top.hendrixshen.magiclib.api.command.client.arguments;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.util.Mth;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 1.16
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;

/**
 * The client-side counterpart of {@code net.minecraft.commands.arguments.AngleArgument}.
 *
 * <p>The parsing logic reuses the vanilla helpers whenever possible. Only the getter method uses a
 * {@link CommandContext} of {@link ClientCommandSource} instead of {@code CommandSourceStack}, and
 * the resolved angle type is client-side because it must resolve against a
 * {@link ClientCommandSource}.</p>
 */
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ClientAngleArgument implements ArgumentType<ClientAngleArgument.SingleAngle> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0", "~", "~-5");
    private static final SimpleCommandExceptionType ERROR_NOT_COMPLETE = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.angle.incomplete"));

    /**
     * Creates an angle argument.
     *
     * @return the argument
     */
    public static ClientAngleArgument angle() {
        return new ClientAngleArgument();
    }

    /**
     * Gets the angle from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the angle
     */
    public static float getAngle(CommandContext<ClientCommandSource> context, String name) {
        return context.<ClientAngleArgument.SingleAngle>getArgument(name, ClientAngleArgument.SingleAngle.class)
                .getAngle(context.getSource());
    }

    @Override
    public ClientAngleArgument.SingleAngle parse(StringReader reader) throws CommandSyntaxException {
        if (!reader.canRead()) {
            throw ClientAngleArgument.ERROR_NOT_COMPLETE.createWithContext(reader);
        }

        //#if MC >= 1.16
        boolean isRelative = WorldCoordinate.isRelative(reader);
        //#else
        //$$ boolean isRelative = ClientAngleArgument.isRelative(reader);
        //#endif
        float value = reader.canRead() && reader.peek() != ' ' ? reader.readFloat() : 0.0F;
        return new ClientAngleArgument.SingleAngle(value, isRelative);
    }

    //#if MC < 1.16
    //$$ private static boolean isRelative(StringReader reader) {
    //$$     boolean isRelative;
    //$$
    //$$     if (reader.peek() == '~') {
    //$$         isRelative = true;
    //$$         reader.skip();
    //$$     } else {
    //$$         isRelative = false;
    //$$     }
    //$$
    //$$     return isRelative;
    //$$ }
    //#endif

    @Override
    public Collection<String> getExamples() {
        return ClientAngleArgument.EXAMPLES;
    }

    /**
     * A single angle value.
     */
    public static final class SingleAngle {
        private final float angle;
        private final boolean isRelative;

        private SingleAngle(float angle, boolean isRelative) {
            this.angle = angle;
            this.isRelative = isRelative;
        }

        /**
         * Resolves the angle against the given source.
         *
         * @param source the client command source
         * @return the resolved angle
         */
        public float getAngle(ClientCommandSource source) {
            return Mth.wrapDegrees(this.isRelative ? this.angle + source.getRotation().y : this.angle);
        }
    }
}
