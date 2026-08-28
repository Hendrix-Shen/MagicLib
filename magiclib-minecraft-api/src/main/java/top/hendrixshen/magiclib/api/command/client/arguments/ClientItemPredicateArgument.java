package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.arguments.item.ItemParser;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 1.18
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.Tag;
//#endif

//#if 1.18 > MC && MC > 1.15.2
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagContainer;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.item.ItemPredicateArgument}.
 *
 * <p>The parsing and suggestion logic is identical to the vanilla implementation. Only the getter
 * method uses a {@link CommandContext} of {@link ClientCommandSource} instead of
 * {@code CommandSourceStack}, resolving tags from the client connection.</p>
 *
 * <li>mc1.14 ~ mc1.18: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.19+        : subproject 1.19.2</li>
 */
public class ClientItemPredicateArgument implements ArgumentType<ClientItemPredicateArgument.Result> {
    private static final Collection<String> EXAMPLES = Arrays.asList("stick", "minecraft:stick", "#stick", "#stick{foo=bar}");
    private static final DynamicCommandExceptionType ERROR_UNKNOWN_TAG = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("arguments.item.tag.unknown", object));

    private ClientItemPredicateArgument() {
    }

    /**
     * Creates an item predicate argument.
     *
     * @return the argument
     */
    public static ClientItemPredicateArgument itemPredicate() {
        return new ClientItemPredicateArgument();
    }

    /**
     * Gets the item predicate from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the item predicate
     * @throws CommandSyntaxException if the tag is unknown
     */
    public static Predicate<ItemStack> getItemPredicate(CommandContext<ClientCommandSource> context, String name) throws CommandSyntaxException {
        return context.<ClientItemPredicateArgument.Result>getArgument(name, ClientItemPredicateArgument.Result.class)
                //#if MC >= 1.18
                //$$ .create(context.getSource().getLevel().registryAccess().registryOrThrow(net.minecraft.core.Registry.ITEM_REGISTRY));
                //#else
                .create(context.getSource().getClient().getConnection().getTags());
                //#endif
    }

    @Override
    public ClientItemPredicateArgument.Result parse(StringReader stringReader) throws CommandSyntaxException {
        ItemParser itemParser = new ItemParser(stringReader, true).parse();

        if (itemParser.getItem() != null) {
            ClientItemPredicateArgument.ItemPredicate itemPredicate = new ClientItemPredicateArgument.ItemPredicate(
                    itemParser.getItem(), itemParser.getNbt());
            return tagContainer -> itemPredicate;
        }

        //#if MC >= 1.18
        //$$ net.minecraft.tags.TagKey<Item> tagKey = itemParser.getTag();
        //$$ return registry -> {
        //$$     if (!registry.isKnownTagName(tagKey)) {
        //$$         throw ClientItemPredicateArgument.ERROR_UNKNOWN_TAG.create(tagKey);
        //$$     }
        //$$
        //$$     return new ClientItemPredicateArgument.TagPredicate(tagKey, itemParser.getNbt());
        //$$ };
        //#else
        ResourceLocation resourceLocation = itemParser.getTag();
        //#if MC >= 1.17
        //$$ return tagContainer -> {
        //$$     Tag<Item> tag = tagContainer.getTagOrThrow(
        //$$             net.minecraft.core.Registry.ITEM_REGISTRY,
        //$$             resourceLocation,
        //$$             resourceLocationx -> ClientItemPredicateArgument.ERROR_UNKNOWN_TAG.create(resourceLocationx.toString()));
        //$$     return new ClientItemPredicateArgument.TagPredicate(tag, itemParser.getNbt());
        //$$ };
        //#else
        return tagContainer -> {
            Tag<Item> tag = tagContainer.getItems().getTag(resourceLocation);

            if (tag == null) {
                throw ClientItemPredicateArgument.ERROR_UNKNOWN_TAG.create(resourceLocation.toString());
            }

            return new ClientItemPredicateArgument.TagPredicate(tag, itemParser.getNbt());
        };
        //#endif
        //#endif
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        StringReader stringReader = new StringReader(builder.getInput());
        stringReader.setCursor(builder.getStart());
        ItemParser itemParser = new ItemParser(stringReader, true);

        try {
            itemParser.parse();
        } catch (CommandSyntaxException e) {
            // ignored
        }

        //#if MC >= 1.18
        //$$ return itemParser.fillSuggestions(builder, net.minecraft.core.Registry.ITEM);
        //#elseif MC >= 1.16
        return itemParser.fillSuggestions(builder, ItemTags.getAllTags());
        //#else
        //$$ return itemParser.fillSuggestions(builder);
        //#endif
    }

    @Override
    public Collection<String> getExamples() {
        return ClientItemPredicateArgument.EXAMPLES;
    }

    static class ItemPredicate implements Predicate<ItemStack> {
        private final Item item;
        private final CompoundTag nbt;

        ItemPredicate(Item item, CompoundTag compoundTag) {
            this.item = item;
            this.nbt = compoundTag;
        }

        @Override
        public boolean test(ItemStack itemStack) {
            return itemStack.getItem() == this.item && NbtUtils.compareNbt(this.nbt, itemStack.getTag(), true);
        }
    }

    /**
     * The result of parsing an item predicate.
     */
    public interface Result {
        /**
         * Creates the predicate with the given tags.
         *
         * @return the predicate
         * @throws CommandSyntaxException if a tag is unknown
         */
        //#if MC >= 1.18
        //$$ Predicate<ItemStack> create(net.minecraft.core.Registry<Item> registry) throws CommandSyntaxException;
        //#elseif MC >= 1.16
        Predicate<ItemStack> create(TagContainer tagContainer) throws CommandSyntaxException;
        //#else
        //$$ Predicate<ItemStack> create(net.minecraft.tags.TagManager tagManager) throws CommandSyntaxException;
        //#endif
    }

    static class TagPredicate implements Predicate<ItemStack> {
        //#if MC >= 1.18
        //$$ private final net.minecraft.tags.TagKey<Item> tag;
        //$$ private final CompoundTag nbt;
        //$$
        //$$ TagPredicate(net.minecraft.tags.TagKey<Item> tag, CompoundTag compoundTag) {
        //$$     this.tag = tag;
        //$$     this.nbt = compoundTag;
        //$$ }
        //#else
        private final Tag<Item> tag;
        private final CompoundTag nbt;

        TagPredicate(Tag<Item> tag, CompoundTag compoundTag) {
            this.tag = tag;
            this.nbt = compoundTag;
        }
        //#endif

        @Override
        public boolean test(ItemStack itemStack) {
            //#if MC >= 1.18
            //$$ return itemStack.is(this.tag) && NbtUtils.compareNbt(this.nbt, itemStack.getTag(), true);
            //#else
            return this.tag.contains(itemStack.getItem()) && NbtUtils.compareNbt(this.nbt, itemStack.getTag(), true);
            //#endif
        }
    }
}
