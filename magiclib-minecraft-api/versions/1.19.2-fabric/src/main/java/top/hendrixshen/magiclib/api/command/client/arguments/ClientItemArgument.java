package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.item.ItemArgument;
import net.minecraft.commands.arguments.item.ItemInput;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.item.ItemArgument}.
 *
 * <p>The parsing and suggestion logic is delegated to the vanilla implementation, since it does not
 * depend on the command source. Only the getter method uses a {@link CommandContext} of
 * {@link ClientCommandSource} instead of {@code CommandSourceStack}.</p>
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.18: subproject 1.16.5 (main project)</li>
 * <li>mc1.19+        : subproject 1.19.2        &lt;--------</li>
 */
public class ClientItemArgument implements ArgumentType<ItemInput> {
    private static final Collection<String> EXAMPLES = Arrays.asList("stick", "minecraft:stick", "stick{foo=bar}");
    private final ItemArgument delegate;

    private ClientItemArgument(ItemArgument delegate) {
        this.delegate = delegate;
    }

    /**
     * Creates an item argument.
     *
     * @return the argument
     */
    public static ClientItemArgument item() {
        return new ClientItemArgument(ItemArgument.item(ClientItemArgument.getBuildContext()));
    }

    /**
     * Gets the item input from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the item input
     */
    public static ItemInput getItem(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, ItemInput.class);
    }

    @Override
    public ItemInput parse(StringReader stringReader) throws CommandSyntaxException {
        return this.delegate.parse(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return this.delegate.listSuggestions(context, builder);
    }

    private static CommandBuildContext getBuildContext() {
        Minecraft client = Minecraft.getInstance();
        //#if MC >= 1.19.3
        //$$ return CommandBuildContext.simple(client.level.registryAccess(), client.level.enabledFeatures());
        //#else
        return new CommandBuildContext(client.level.registryAccess());
        //#endif
    }

    @Override
    public Collection<String> getExamples() {
        return ClientItemArgument.EXAMPLES;
    }
}
