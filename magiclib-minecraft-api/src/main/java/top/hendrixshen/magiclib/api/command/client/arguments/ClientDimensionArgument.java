package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 1.21.11
//$$ import net.minecraft.resources.Identifier;
//#else
import net.minecraft.resources.ResourceLocation;
//#endif

//#if 1.19.3 > MC && MC > 1.15.2
import net.minecraft.core.Registry;
//#endif

//#if MC >= 1.19.3
//$$ import net.minecraft.core.registries.Registries;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.DimensionArgument}.
 *
 * <p>The parsing and suggestion logic is identical to the vanilla implementation. Unlike the vanilla
 * getter, which returns a {@code ServerLevel}, the client-side getter returns the dimension
 * {@link ResourceKey}, since the client has no server levels.</p>
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.15: subproject 1.15.2</li>
 * <li>mc1.16+        : subproject 1.16.5 (main project)        &lt;--------</li>
 */
public class ClientDimensionArgument implements ArgumentType<ResourceLocation> {
    private static final Collection<String> EXAMPLES = Stream.of(Level.OVERWORLD, Level.NETHER)
            .map(resourceKey -> resourceKey.location().toString())
            .collect(Collectors.toList());
    private static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("argument.dimension.invalid", object));

    private ClientDimensionArgument() {
    }

    /**
     * Creates a dimension argument.
     *
     * @return the argument
     */
    public static ClientDimensionArgument dimension() {
        return new ClientDimensionArgument();
    }

    /**
     * Gets the dimension key from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the dimension key
     * @throws CommandSyntaxException if the dimension is invalid
     */
    public static ResourceKey<Level> getDimension(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        ResourceLocation resourceLocation = context.getArgument(name, ResourceLocation.class);
        //#if MC >= 1.19.3
        //$$ return ResourceKey.create(Registries.DIMENSION, resourceLocation);
        //#else
        return ResourceKey.create(Registry.DIMENSION_REGISTRY, resourceLocation);
        //#endif
    }

    @Override
    public ResourceLocation parse(StringReader stringReader) throws CommandSyntaxException {
        return ResourceLocation.read(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return context.getSource() instanceof SharedSuggestionProvider
                ? SharedSuggestionProvider.suggestResource(
                        ((SharedSuggestionProvider) context.getSource()).levels().stream().map(ResourceKey::location), builder)
                : Suggestions.empty();
    }

    @Override
    public Collection<String> getExamples() {
        return ClientDimensionArgument.EXAMPLES;
    }
}
