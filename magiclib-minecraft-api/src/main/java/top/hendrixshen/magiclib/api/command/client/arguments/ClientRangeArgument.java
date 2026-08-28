package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 26.2
//$$ import net.minecraft.advancements.predicates.MinMaxBounds;
//#else
import net.minecraft.advancements.critereon.MinMaxBounds;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Arrays;
import java.util.Collection;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.RangeArgument}.
 *
 * <p>The parsing logic is identical to the vanilla implementation: it is delegated to the pure
 * parsers {@link MinMaxBounds.Ints#fromReader(StringReader)} and
 * {@link MinMaxBounds.Floats#fromReader(StringReader)}, which do not depend on the command source.
 * Only the getter methods use a {@link CommandContext} of {@link ClientCommandSource} instead of
 * {@code CommandSourceStack}.</p>
 *
 * <li>mc1.14 ~ mc26.1: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc26.2+        : subproject 26.2</li>
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
     *
     * <p>Parses a float range from the reader and provides the getter for the parsed value.</p>
     */
    class Floats implements ClientRangeArgument<MinMaxBounds.Floats> {
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
        public static MinMaxBounds.Floats getRange(CommandContext<ClientCommandSource> context, String name) {
            return context.getArgument(name, MinMaxBounds.Floats.class);
        }

        /**
         * Parses a float range from the reader.
         *
         * @param stringReader the reader
         * @return the float range
         * @throws CommandSyntaxException if the reader contains an invalid float range
         */
        @Override
        public MinMaxBounds.Floats parse(StringReader stringReader) throws CommandSyntaxException {
            return MinMaxBounds.Floats.fromReader(stringReader);
        }

        /**
         * Gets the example values for this argument type, useful for tooltip documentation.
         *
         * @return the example strings
         */
        @Override
        public Collection<String> getExamples() {
            return ClientRangeArgument.Floats.EXAMPLES;
        }
    }

    /**
     * The client-side counterpart of the vanilla int range argument.
     *
     * <p>Parses an int range from the reader and provides the getter for the parsed value.</p>
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

        /**
         * Parses an int range from the reader.
         *
         * @param stringReader the reader
         * @return the int range
         * @throws CommandSyntaxException if the reader contains an invalid int range
         */
        @Override
        public MinMaxBounds.Ints parse(StringReader stringReader) throws CommandSyntaxException {
            return MinMaxBounds.Ints.fromReader(stringReader);
        }

        /**
         * Gets the example values for this argument type, useful for tooltip documentation.
         *
         * @return the example strings
         */
        @Override
        public Collection<String> getExamples() {
            return ClientRangeArgument.Ints.EXAMPLES;
        }
    }
}
