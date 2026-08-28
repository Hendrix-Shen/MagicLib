package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.network.chat.Component;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 1.20.4
//$$ import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
//$$ import net.minecraft.commands.ParserUtils;
//$$ import net.minecraft.network.chat.ComponentSerialization;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

// CHECKSTYLE.OFF: ImportOrder
//#if 1.20.5 > MC && MC > 1.20.3
//$$ import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;
//#endif
// CHECKSTYLE.ON: ImportOrder

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
 * <li>mc1.14 ~ mc1.20.4: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.20.5+        : subproject 1.20.6</li>
 */
public class ClientComponentArgument implements ArgumentType<Component> {
    private static final Collection<String> EXAMPLES = Arrays.asList("\"hello world\"", "\"\"", "\"{\"text\":\"hello world\"}", "[\"\"]");
    //#if MC >= 1.20.4
    //$$ private static final DynamicCommandExceptionType ERROR_INVALID_JSON = new DynamicCommandExceptionType(
    //$$         object -> ComponentCompat.translatable("argument.component.invalid", object));
    //#endif

    private ClientComponentArgument() {
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
     * @return the argument
     */
    public static ClientComponentArgument textComponent() {
        return new ClientComponentArgument();
    }

    @Override
    public Component parse(StringReader stringReader) throws CommandSyntaxException {
        //#if MC >= 1.20.4
        //$$ try {
        //$$     return ParserUtils.parseJson(stringReader, ComponentSerialization.CODEC);
        //$$ } catch (Exception e) {
        //$$     String string = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
        //$$     throw ClientComponentArgument.ERROR_INVALID_JSON.createWithContext(stringReader, string);
        //$$ }
        //#else
        return net.minecraft.commands.arguments.ComponentArgument.textComponent().parse(stringReader);
        //#endif
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return net.minecraft.commands.arguments.ComponentArgument.textComponent().listSuggestions(context, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return ClientComponentArgument.EXAMPLES;
    }
}
