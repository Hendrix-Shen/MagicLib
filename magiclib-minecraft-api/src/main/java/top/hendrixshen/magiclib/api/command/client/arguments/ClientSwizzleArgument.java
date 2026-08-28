package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.core.Direction;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.coordinates.SwizzleArgument}.
 *
 * <p>The parsing logic is delegated to the vanilla implementation, since it does not depend on the
 * command source. Only the getter methods use a {@link CommandContext} of {@link ClientCommandSource}
 * instead of {@code CommandSourceStack}.</p>
 */
public class ClientSwizzleArgument implements ArgumentType<EnumSet<Direction.Axis>> {
    private static final Collection<String> EXAMPLES = Arrays.asList("xyz", "x");

    private ClientSwizzleArgument() {
    }

    /**
     * Creates a swizzle argument.
     *
     * @return the argument
     */
    public static ClientSwizzleArgument swizzle() {
        return new ClientSwizzleArgument();
    }

    /**
     * Gets the swizzle from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the swizzle
     */
    @SuppressWarnings("unchecked")
    public static EnumSet<Direction.Axis> getSwizzle(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, EnumSet.class);
    }

    @Override
    public EnumSet<Direction.Axis> parse(StringReader stringReader) throws CommandSyntaxException {
        return net.minecraft.commands.arguments.coordinates.SwizzleArgument.swizzle().parse(stringReader);
    }

    @Override
    public Collection<String> getExamples() {
        return ClientSwizzleArgument.EXAMPLES;
    }
}
