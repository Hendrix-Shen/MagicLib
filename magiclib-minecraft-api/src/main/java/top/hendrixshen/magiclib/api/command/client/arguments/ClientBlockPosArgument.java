package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 1.15
import net.minecraft.client.multiplayer.ClientLevel;
//#else
//$$ import net.minecraft.client.multiplayer.MultiPlayerLevel;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.coordinates.BlockPosArgument}.
 *
 * <p>The parsing and suggestion logic is identical to the vanilla implementation. Only the getter
 * methods use a {@link CommandContext} of {@link ClientCommandSource} instead of
 * {@code CommandSourceStack}, and resolve the chunk/world-bound checks against the client level.</p>
 */
public class ClientBlockPosArgument implements ArgumentType<ClientCoordinates> {
    private static final Collection<String> EXAMPLES = Arrays.asList("0 0 0", "~ ~ ~", "^ ^ ^", "^1 ^ ^-5", "~0.5 ~1 ~-5");
    private static final SimpleCommandExceptionType ERROR_NOT_LOADED = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.pos.unloaded"));
    private static final SimpleCommandExceptionType ERROR_OUT_OF_WORLD = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.pos.outofworld"));
    private static final SimpleCommandExceptionType ERROR_OUT_OF_BOUNDS = new SimpleCommandExceptionType(
            ComponentCompat.translatable("argument.pos.outofbounds"));

    private ClientBlockPosArgument() {
    }

    /**
     * Creates a block pos argument.
     *
     * @return the argument
     */
    public static ClientBlockPosArgument blockPos() {
        return new ClientBlockPosArgument();
    }

    /**
     * Gets the block pos from the context, checking that the chunk is loaded and the position is
     * inside the world bounds.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the block pos
     * @throws CommandSyntaxException if the chunk is not loaded or the position is out of bounds
     */
    public static BlockPos getLoadedBlockPos(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        BlockPos blockPos = ClientBlockPosArgument.getBlockPos(context, name);
        //#if MC >= 1.15
        ClientLevel level = context.getSource().getLevel();
        //#else
        //$$ MultiPlayerLevel level = context.getSource().getLevel();
        //#endif

        if (!level.hasChunk(blockPos.getX() >> 4, blockPos.getZ() >> 4)) {
            throw ClientBlockPosArgument.ERROR_NOT_LOADED.create();
        }

        if (
                //#if MC > 1.17
                //$$ !level.isInWorldBounds(blockPos)
                //#else
                !Level.isInWorldBounds(blockPos)
                //#endif
        ) {
            throw ClientBlockPosArgument.ERROR_OUT_OF_WORLD.create();
        }

        return blockPos;
    }

    /**
     * Gets the block pos from the context without any chunk or bounds check.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the block pos
     */
    public static BlockPos getBlockPos(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, ClientCoordinates.class).getBlockPos(context.getSource());
    }

    //#if MC >= 1.16
    /**
     * Gets the block pos from the context, checking that the position is inside the spawnable
     * bounds.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the block pos
     * @throws CommandSyntaxException if the position is out of bounds
     */
    public static BlockPos getSpawnablePos(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        BlockPos blockPos = ClientBlockPosArgument.getBlockPos(context, name);

        if (!Level.isInSpawnableBounds(blockPos)) {
            throw ClientBlockPosArgument.ERROR_OUT_OF_BOUNDS.create();
        }

        return blockPos;
    }
    //#endif

    @Override
    public ClientCoordinates parse(StringReader stringReader) throws CommandSyntaxException {
        return stringReader.canRead() && stringReader.peek() == '^'
                ? ClientLocalCoordinates.parse(stringReader)
                : ClientWorldCoordinates.parse(stringReader);
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        if (!(context.getSource() instanceof SharedSuggestionProvider)) {
            return Suggestions.empty();
        }

        String input = builder.getRemaining();
        Collection<SharedSuggestionProvider.TextCoordinates> coordinates;

        if (!input.isEmpty() && input.charAt(0) == '^') {
            coordinates = Collections.singleton(SharedSuggestionProvider.TextCoordinates.DEFAULT_LOCAL);
        } else {
            coordinates = ((SharedSuggestionProvider) context.getSource()).getRelevantCoordinates();
        }

        return SharedSuggestionProvider.suggestCoordinates(input, coordinates, builder, Commands.createValidator(this::parse));
    }

    @Override
    public Collection<String> getExamples() {
        return ClientBlockPosArgument.EXAMPLES;
    }
}
