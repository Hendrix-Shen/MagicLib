package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.arguments.SlotArgument;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.SlotArgument}.
 *
 * <p>The parsing and suggestion logic is delegated to a vanilla {@link SlotArgument} instance, as both only
 * perform pure parsing and do not depend on the command source. Only the getter method uses a
 * {@link CommandContext} of {@link ClientCommandSource} instead of {@code CommandSourceStack}.</p>
 */
public class ClientSlotArgument implements ArgumentType<Integer> {
    private static final Collection<String> EXAMPLES = Arrays.asList("container.5", "12", "weapon");
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_SLOT = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("slot.unknown", object));

    private ClientSlotArgument() {
    }

    /**
     * Creates a slot argument.
     *
     * @return the argument
     */
    public static ClientSlotArgument slot() {
        return new ClientSlotArgument();
    }

    /**
     * Gets the slot from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the slot
     */
    public static int getSlot(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, Integer.class);
    }

    /**
     * Parses a slot from the reader, delegating to the vanilla implementation.
     *
     * @param stringReader the string reader
     * @return the parsed slot
     * @throws CommandSyntaxException if the slot is unknown
     */
    @Override
    public Integer parse(StringReader stringReader) throws CommandSyntaxException {
        return SlotArgument.slot().parse(stringReader);
    }

    /**
     * Suggests the known slot names, delegating to the vanilla implementation.
     *
     * @param context            the command context
     * @param suggestionsBuilder the suggestions builder
     * @param <S>                the type of the command source
     * @return the suggestions for the current input
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder suggestionsBuilder) {
        return SlotArgument.slot().listSuggestions(context, suggestionsBuilder);
    }

    /**
     * Gets the example strings for this argument.
     *
     * @return the example strings
     */
    @Override
    public Collection<String> getExamples() {
        return ClientSlotArgument.EXAMPLES;
    }
}
