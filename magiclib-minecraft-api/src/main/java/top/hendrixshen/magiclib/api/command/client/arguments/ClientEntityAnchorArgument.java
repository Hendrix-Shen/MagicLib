package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.arguments.EntityAnchorArgument;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.EntityAnchorArgument}.
 *
 * <p>The parsing logic is identical to the vanilla implementation. As the vanilla {@code parse} and
 * {@code listSuggestions} are pure parsing methods that do not depend on the command source, this
 * implementation simply delegates to a vanilla argument instance. Only the getter method uses a
 * {@link CommandContext} of {@link ClientCommandSource} instead of {@code CommandSourceStack}.</p>
 */
public class ClientEntityAnchorArgument implements ArgumentType<EntityAnchorArgument.Anchor> {
    private static final Collection<String> EXAMPLES = Arrays.asList("eyes", "feet");
    private static final DynamicCommandExceptionType ERROR_INVALID = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("argument.anchor.invalid", object));

    private ClientEntityAnchorArgument() {
    }

    /**
     * Creates an entity anchor argument.
     *
     * @return the argument
     */
    public static ClientEntityAnchorArgument anchor() {
        return new ClientEntityAnchorArgument();
    }

    /**
     * Gets the entity anchor from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the entity anchor
     */
    public static EntityAnchorArgument.Anchor getAnchor(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, EntityAnchorArgument.Anchor.class);
    }

    /**
     * Parses an entity anchor from the reader.
     *
     * @param stringReader the string reader
     * @return the parsed entity anchor
     * @throws CommandSyntaxException if the input is not a valid entity anchor
     */
    @Override
    public EntityAnchorArgument.Anchor parse(StringReader stringReader) throws CommandSyntaxException {
        return EntityAnchorArgument.anchor().parse(stringReader);
    }

    /**
     * Suggests entity anchors for the current input.
     *
     * @param commandContext     the command context
     * @param suggestionsBuilder the suggestions builder
     * @param <S>                the type of the command source
     * @return the suggestions
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) {
        return EntityAnchorArgument.anchor().listSuggestions(commandContext, suggestionsBuilder);
    }

    /**
     * Gets the examples of this argument type.
     *
     * @return the examples
     */
    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
