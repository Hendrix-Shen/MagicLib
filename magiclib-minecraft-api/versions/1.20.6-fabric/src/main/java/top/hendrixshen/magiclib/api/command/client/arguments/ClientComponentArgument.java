package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.ComponentArgument;
import net.minecraft.network.chat.Component;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.ComponentArgument}.
 *
 * <p>The parsing and suggestion logic is delegated to the vanilla implementation, since it does not
 * depend on the command source. Only the getter method uses a {@link CommandContext} of
 * {@link ClientCommandSource} instead of {@code CommandSourceStack}.</p>
 *
 * <li>mc1.14 ~ mc1.20.4: subproject 1.16.5 (main project)</li>
 * <li>mc1.20.5+        : subproject 1.20.6        &lt;--------</li>
 */
public class ClientComponentArgument implements ArgumentType<Component> {
    private static final Collection<String> EXAMPLES = Arrays.asList("\"hello world\"", "'hello world'", "\"\"", "{text:\"hello world\"}", "[\"\"]");
    private final ComponentArgument delegate;

    private ClientComponentArgument(ComponentArgument delegate) {
        this.delegate = delegate;
    }

    /**
     * Gets the component from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the component
     */
    public static Component getComponent(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, Component.class);
    }

    /**
     * Creates a component argument.
     *
     * @param buildContext the command build context
     * @return the argument
     */
    public static ClientComponentArgument textComponent(CommandBuildContext buildContext) {
        return new ClientComponentArgument(ComponentArgument.textComponent(buildContext));
    }

    @Override
    public Component parse(StringReader stringReader) throws CommandSyntaxException {
        return this.delegate.parse(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return this.delegate.listSuggestions(context, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return ClientComponentArgument.EXAMPLES;
    }
}
