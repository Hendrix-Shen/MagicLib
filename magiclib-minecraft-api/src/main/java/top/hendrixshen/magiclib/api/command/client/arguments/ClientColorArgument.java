package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.ColorArgument;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.ColorArgument}.
 *
 * <p>The parsing and suggestion logic is delegated to the vanilla implementation, which only
 * performs pure parsing and does not depend on the command source. Only the getter method uses a
 * {@link CommandContext} of {@link ClientCommandSource} instead of
 * {@code CommandSourceStack}.</p>
 *
 * <li>mc1.14 ~ mc26.1: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc26.2+        : subproject 26.2</li>
 */
public class ClientColorArgument implements ArgumentType<ChatFormatting> {
    private static final Collection<String> EXAMPLES = Arrays.asList("red", "green");
    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("argument.color.invalid", object)
    );

    private ClientColorArgument() {
    }

    /**
     * Creates a color argument.
     *
     * @return the argument
     */
    public static ClientColorArgument color() {
        return new ClientColorArgument();
    }

    /**
     * Gets the color from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the color
     */
    public static ChatFormatting getColor(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, ChatFormatting.class);
    }

    /**
     * Parses a color name from the reader.
     *
     * @param stringReader the string reader
     * @return the color
     * @throws CommandSyntaxException if the value is not a valid color
     */
    @Override
    public ChatFormatting parse(StringReader stringReader) throws CommandSyntaxException {
        return ColorArgument.color().parse(stringReader);
    }

    /**
     * Suggests the available color names.
     *
     * @param context the command context
     * @param builder the suggestions builder
     * @return the suggestions
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return ColorArgument.color().listSuggestions(context, builder);
    }

    /**
     * Gets the example values for this argument.
     *
     * @return the example values
     */
    @Override
    public Collection<String> getExamples() {
        return ClientColorArgument.EXAMPLES;
    }
}
