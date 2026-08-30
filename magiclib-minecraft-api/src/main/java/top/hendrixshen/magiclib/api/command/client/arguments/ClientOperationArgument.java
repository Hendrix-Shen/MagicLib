package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.OperationArgument;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.OperationArgument}.
 *
 * <p>The parsing logic is delegated to the vanilla implementation, which is a pure parser that
 * does not depend on the command source. Only the getter method uses a {@link CommandContext} of
 * {@link ClientCommandSource} instead of {@code CommandSourceStack}.</p>
 */
public class ClientOperationArgument implements ArgumentType<OperationArgument.Operation> {
    private static final Collection<String> EXAMPLES = Arrays.asList("=", ">", "<");
    private static final SimpleCommandExceptionType ERROR_INVALID_OPERATION = new SimpleCommandExceptionType(
            ComponentCompat.translatable("arguments.operation.invalid"));
    private static final SimpleCommandExceptionType ERROR_DIVIDE_BY_ZERO = new SimpleCommandExceptionType(
            ComponentCompat.translatable("arguments.operation.div0"));

    private ClientOperationArgument() {
    }

    /**
     * Creates an operation argument.
     *
     * @return the argument
     */
    public static ClientOperationArgument operation() {
        return new ClientOperationArgument();
    }

    /**
     * Gets the operation from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the operation
     * @throws CommandSyntaxException if the argument is not present
     */
    public static OperationArgument.Operation getOperation(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        return context.getArgument(name, OperationArgument.Operation.class);
    }

    @Override
    public OperationArgument.Operation parse(StringReader stringReader) throws CommandSyntaxException {
        return OperationArgument.operation().parse(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(new String[]{"=", "+=", "-=", "*=", "/=", "%=", "<", ">", "><"}, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return ClientOperationArgument.EXAMPLES;
    }
}
