package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.ParticleArgument}.
 *
 * <p>The parsing and suggestion logic is identical to the vanilla implementation. Only the getter
 * method uses a {@link CommandContext} of {@link ClientCommandSource} instead of
 * {@code CommandSourceStack}.</p>
 *
 * <li>mc1.14 ~ mc1.19.2  : subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.19.3 ~ mc1.20.4: subproject 1.19.3</li>
 * <li>mc1.20.5+          : subproject 1.20.6</li>
 */
public class ClientParticleArgument implements ArgumentType<ParticleOptions> {
    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "particle with options");
    public static final DynamicCommandExceptionType ERROR_UNKNOWN_PARTICLE = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("particle.notFound", object));

    private ClientParticleArgument() {
    }

    /**
     * Creates a particle argument.
     *
     * @return the argument
     */
    public static ClientParticleArgument particle() {
        return new ClientParticleArgument();
    }

    /**
     * Gets the particle options from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the particle options
     */
    public static ParticleOptions getParticle(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, ParticleOptions.class);
    }

    /**
     * Parses the particle from the reader.
     *
     * @param reader the string reader
     * @return the parsed particle options
     * @throws CommandSyntaxException if the particle is unknown or its options are invalid
     */
    @Override
    public ParticleOptions parse(StringReader reader) throws CommandSyntaxException {
        return ParticleArgument.particle().parse(reader);
    }

    /**
     * Suggests the resource locations of all registered particle types.
     *
     * @param context the command context
     * @param builder the suggestion builder
     * @param <S>     the source type
     * @return the suggestions
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggestResource(Registry.PARTICLE_TYPE.keySet(), builder);
    }

    /**
     * Gets the examples.
     *
     * @return the examples
     */
    @Override
    public Collection<String> getExamples() {
        return ClientParticleArgument.EXAMPLES;
    }
}
