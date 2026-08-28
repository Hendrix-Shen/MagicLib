package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.pattern.BlockInWorld;
import net.minecraft.world.level.block.state.properties.Property;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 1.18
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
//#endif

//#if 1.18 > MC && MC > 1.15.2
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagContainer;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.blocks.BlockPredicateArgument}.
 *
 * <p>The parsing and suggestion logic is identical to the vanilla implementation. Only the getter
 * method uses a {@link CommandContext} of {@link ClientCommandSource} instead of
 * {@code CommandSourceStack}, resolving tags from the client connection.</p>
 *
 * <li>mc1.14 ~ mc1.18: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.19+        : subproject 1.19.2</li>
 */
public class ClientBlockPredicateArgument implements ArgumentType<ClientBlockPredicateArgument.Result> {
    private static final Collection<String> EXAMPLES = Arrays.asList("stone", "minecraft:stone", "stone[foo=bar]", "#stone", "#stone[foo=bar]{baz=nbt}");
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("arguments.block.tag.unknown", object));

    private ClientBlockPredicateArgument() {
    }

    /**
     * Creates a block predicate argument.
     *
     * @return the argument
     */
    public static ClientBlockPredicateArgument blockPredicate() {
        return new ClientBlockPredicateArgument();
    }

    /**
     * Gets the block predicate from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the block predicate
     * @throws CommandSyntaxException if the tag is unknown
     */
    public static Predicate<BlockInWorld> getBlockPredicate(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        return context.<ClientBlockPredicateArgument.Result>getArgument(name, ClientBlockPredicateArgument.Result.class)
                //#if MC >= 1.18
                //$$ .create(context.getSource().getLevel().registryAccess().registryOrThrow(net.minecraft.core.Registry.BLOCK_REGISTRY));
                //#else
                .create(context.getSource().getClient().getConnection().getTags());
                //#endif
    }

    @Override
    public ClientBlockPredicateArgument.Result parse(StringReader stringReader) throws CommandSyntaxException {
        BlockStateParser blockStateParser = new BlockStateParser(stringReader, true).parse(true);

        if (blockStateParser.getState() != null) {
            ClientBlockPredicateArgument.BlockPredicate blockPredicate = new ClientBlockPredicateArgument.BlockPredicate(
                    blockStateParser.getState(), blockStateParser.getProperties().keySet(), blockStateParser.getNbt());
            return tagContainer -> blockPredicate;
        }

        //#if MC >= 1.18
        //$$ net.minecraft.tags.TagKey<Block> tagKey = blockStateParser.getTag();
        //$$ return registry -> {
        //$$     if (!registry.isKnownTagName(tagKey)) {
        //$$         throw ClientBlockPredicateArgument.ERROR_UNKNOWN_TAG.create(tagKey);
        //$$     }
        //$$
        //$$     return new ClientBlockPredicateArgument.TagPredicate(tagKey, blockStateParser.getVagueProperties(), blockStateParser.getNbt());
        //$$ };
        //#elseif MC >= 1.17
        //$$ ResourceLocation resourceLocation = blockStateParser.getTag();
        //$$ return tagContainer -> {
        //$$     Tag<Block> tag = tagContainer.getTagOrThrow(
        //$$             net.minecraft.core.Registry.BLOCK_REGISTRY,
        //$$             resourceLocation,
        //$$             resourceLocationx -> ClientBlockPredicateArgument.ERROR_UNKNOWN_TAG.create(resourceLocationx.toString()));
        //$$     return new ClientBlockPredicateArgument.TagPredicate(tag, blockStateParser.getVagueProperties(), blockStateParser.getNbt());
        //$$ };
        //#else
        ResourceLocation resourceLocation = blockStateParser.getTag();
        return tagContainer -> {
            Tag<Block> tag = tagContainer.getBlocks().getTag(resourceLocation);

            if (tag == null) {
                throw ClientBlockPredicateArgument.ERROR_UNKNOWN_TAG.create(resourceLocation.toString());
            }

            return new ClientBlockPredicateArgument.TagPredicate(tag, blockStateParser.getVagueProperties(), blockStateParser.getNbt());
        };
        //#endif
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        StringReader stringReader = new StringReader(builder.getInput());
        stringReader.setCursor(builder.getStart());
        BlockStateParser blockStateParser = new BlockStateParser(stringReader, true);

        try {
            blockStateParser.parse(true);
        } catch (CommandSyntaxException e) {
            // ignored
        }

        //#if MC >= 1.18
        //$$ return blockStateParser.fillSuggestions(builder, net.minecraft.core.Registry.BLOCK);
        //#elseif MC >= 1.16
        return blockStateParser.fillSuggestions(builder, BlockTags.getAllTags());
        //#else
        //$$ return blockStateParser.fillSuggestions(builder);
        //#endif
    }

    @Override
    public Collection<String> getExamples() {
        return ClientBlockPredicateArgument.EXAMPLES;
    }

    static class BlockPredicate implements Predicate<BlockInWorld> {
        private final BlockState state;
        private final Set<Property<?>> properties;
        private final CompoundTag nbt;

        BlockPredicate(BlockState blockState, Set<Property<?>> set, CompoundTag compoundTag) {
            this.state = blockState;
            this.properties = set;
            this.nbt = compoundTag;
        }

        @Override
        public boolean test(BlockInWorld blockInWorld) {
            BlockState blockState = blockInWorld.getState();

            if (
                    //#if MC >= 1.16
                    !blockState.is(this.state.getBlock())
                    //#else
                    //$$ blockState.getBlock() != this.state.getBlock()
                    //#endif
            ) {
                return false;
            }

            for (Property<?> property : this.properties) {
                if (blockState.getValue(property) != this.state.getValue(property)) {
                    return false;
                }
            }

            if (this.nbt == null) {
                return true;
            }

            BlockEntity blockEntity = blockInWorld.getEntity();
            return blockEntity != null && NbtUtils.compareNbt(
                    this.nbt,
                    //#if MC >= 1.18
                    //$$ blockEntity.saveWithFullMetadata(),
                    //#else
                    blockEntity.save(new CompoundTag()),
                    //#endif
                    true
            );
        }
    }

    /**
     * The result of parsing a block predicate.
     */
    public interface Result {
        /**
         * Creates the predicate with the given tags.
         *
         * @return the predicate
         * @throws CommandSyntaxException if a tag is unknown
         */
        //#if MC >= 1.18
        //$$ Predicate<BlockInWorld> create(net.minecraft.core.Registry<Block> registry) throws CommandSyntaxException;
        //#elseif MC >= 1.16
        Predicate<BlockInWorld> create(TagContainer tagContainer) throws CommandSyntaxException;
        //#else
        //$$ Predicate<BlockInWorld> create(net.minecraft.tags.TagManager tagManager) throws CommandSyntaxException;
        //#endif
    }

    static class TagPredicate implements Predicate<BlockInWorld> {
        //#if MC >= 1.18
        //$$ private final net.minecraft.tags.TagKey<Block> tag;
        //$$ private final CompoundTag nbt;
        //$$ private final Map<String, String> vagueProperties;
        //$$
        //$$ TagPredicate(net.minecraft.tags.TagKey<Block> tag, Map<String, String> map, CompoundTag compoundTag) {
        //$$     this.tag = tag;
        //$$     this.vagueProperties = map;
        //$$     this.nbt = compoundTag;
        //$$ }
        //#else
        private final Tag<Block> tag;
        private final CompoundTag nbt;
        private final Map<String, String> vagueProperties;

        TagPredicate(Tag<Block> tag, Map<String, String> map, CompoundTag compoundTag) {
            this.tag = tag;
            this.vagueProperties = map;
            this.nbt = compoundTag;
        }
        //#endif

        @Override
        public boolean test(BlockInWorld blockInWorld) {
            BlockState blockState = blockInWorld.getState();

            if (!blockState.is(this.tag)) {
                return false;
            }

            for (Entry<String, String> entry : this.vagueProperties.entrySet()) {
                Property<?> property = blockState.getBlock().getStateDefinition().getProperty(entry.getKey());

                if (property == null) {
                    return false;
                }

                Comparable<?> comparable = (Comparable<?>) property.getValue(entry.getValue()).orElse(null);

                if (comparable == null) {
                    return false;
                }

                if (blockState.getValue(property) != comparable) {
                    return false;
                }
            }

            if (this.nbt == null) {
                return true;
            }

            BlockEntity blockEntity = blockInWorld.getEntity();
            return blockEntity != null && NbtUtils.compareNbt(
                    this.nbt,
                    //#if MC >= 1.18
                    //$$ blockEntity.saveWithFullMetadata(),
                    //#else
                    blockEntity.save(new CompoundTag()),
                    //#endif
                    true
            );
        }
    }
}
