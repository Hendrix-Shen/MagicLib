package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.arguments.ParticleArgument;
import net.minecraft.core.particles.ParticleOptions;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.ParticleArgument}
 * for MC 1.20.6+.
 *
 * <p>The parsing logic is delegated to the vanilla implementation. Only the getter method uses a
 * {@link CommandContext} of {@link ClientCommandSource} instead of {@code CommandSourceStack}.</p>
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.20.4: subproject 1.16.5 (main project)</li>
 * <li>mc1.20.5+        : subproject 1.20.6        &lt;--------</li>
 */
public class ClientParticleArgument implements ArgumentType<ParticleOptions> {
    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo:bar", "particle{foo:bar}");
    private final ParticleArgument delegate;

    private ClientParticleArgument(ParticleArgument delegate) {
        this.delegate = delegate;
    }

    /**
     * Creates a particle argument.
     *
     * @param buildContext the command build context
     * @return the argument
     */
    public static ClientParticleArgument particle(CommandBuildContext buildContext) {
        return new ClientParticleArgument(ParticleArgument.particle(buildContext));
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

    @Override
    public ParticleOptions parse(StringReader stringReader) throws CommandSyntaxException {
        return this.delegate.parse(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return this.delegate.listSuggestions(context, builder);
    }

    @Override
    public Collection<String> getExamples() {
        return ClientParticleArgument.EXAMPLES;
    }
}
