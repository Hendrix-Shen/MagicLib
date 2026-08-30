package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.advancements.predicates.MinMaxBounds;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Arrays;
import java.util.Collection;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.RangeArgument}
 * for MC 26.2+.
 *
 * <p>The parsing logic is identical to the vanilla implementation. Only the getter methods use a
 * {@link CommandContext} of {@link ClientCommandSource} instead of {@code CommandSourceStack}.</p>
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc26.1: subproject 1.16.5 (main project)</li>
 * <li>mc26.2+        : subproject 26.2        &lt;--------</li>
 */
public interface ClientRangeArgument<T extends MinMaxBounds<?>> extends ArgumentType<T> {
    /**
     * Creates an int range argument.
     *
     * @return the argument
     */
    static ClientRangeArgument.Ints intRange() {
        return new ClientRangeArgument.Ints();
    }

    /**
     * Creates a float range argument.
     *
     * @return the argument
     */
    static ClientRangeArgument.Floats floatRange() {
        return new ClientRangeArgument.Floats();
    }

    /**
     * The client-side counterpart of the vanilla float range argument.
     */
    class Floats implements ClientRangeArgument<MinMaxBounds.Doubles> {
        private static final Collection<String> EXAMPLES = Arrays.asList("0..5.2", "0", "-5.4", "-100.76..", "..100");

        private Floats() {
        }

        /**
         * Gets the float range from the command context.
         *
         * @param context the command context
         * @param name    the argument name
         * @return the float range
         */
        public static MinMaxBounds.Doubles getRange(CommandContext<ClientCommandSource> context, String name) {
            return context.getArgument(name, MinMaxBounds.Doubles.class);
        }

        @Override
        public MinMaxBounds.Doubles parse(StringReader stringReader) throws CommandSyntaxException {
            return MinMaxBounds.Doubles.fromReader(stringReader);
        }

        @Override
        public Collection<String> getExamples() {
            return ClientRangeArgument.Floats.EXAMPLES;
        }
    }

    /**
     * The client-side counterpart of the vanilla int range argument.
     */
    class Ints implements ClientRangeArgument<MinMaxBounds.Ints> {
        private static final Collection<String> EXAMPLES = Arrays.asList("0..5", "0", "-5", "-100..", "..100");

        private Ints() {
        }

        /**
         * Gets the int range from the command context.
         *
         * @param context the command context
         * @param name    the argument name
         * @return the int range
         */
        public static MinMaxBounds.Ints getRange(CommandContext<ClientCommandSource> context, String name) {
            return context.getArgument(name, MinMaxBounds.Ints.class);
        }

        @Override
        public MinMaxBounds.Ints parse(StringReader stringReader) throws CommandSyntaxException {
            return MinMaxBounds.Ints.fromReader(stringReader);
        }

        @Override
        public Collection<String> getExamples() {
            return ClientRangeArgument.Ints.EXAMPLES;
        }
    }
}
