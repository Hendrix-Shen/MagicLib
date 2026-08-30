package top.hendrixshen.magiclib.api.command.client.arguments;

import com.google.common.collect.Lists;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.arguments.ObjectiveCriteriaArgument;
import net.minecraft.stats.Stat;
import net.minecraft.stats.StatType;
import net.minecraft.world.scores.criteria.ObjectiveCriteria;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 1.19.3
//$$ import net.minecraft.core.registries.BuiltInRegistries;
//#else
import net.minecraft.core.Registry;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.ObjectiveCriteriaArgument}.
 *
 * <p>The parsing logic is identical to the vanilla implementation, and the suggestion logic mirrors it.
 * Only the getter method uses a {@link CommandContext} of {@link ClientCommandSource} instead of
 * {@code CommandSourceStack}.</p>
 */
public class ClientObjectiveCriteriaArgument implements ArgumentType<ObjectiveCriteria> {
    private static final Collection<String> EXAMPLES = Arrays.asList("foo", "foo.bar.baz", "minecraft:foo");
    public static final DynamicCommandExceptionType ERROR_INVALID_VALUE = new DynamicCommandExceptionType(
            object -> ComponentCompat.translatable("argument.criteria.invalid", object));

    private ClientObjectiveCriteriaArgument() {
    }

    /**
     * Creates an objective criteria argument.
     *
     * @return the argument
     */
    public static ClientObjectiveCriteriaArgument criteria() {
        return new ClientObjectiveCriteriaArgument();
    }

    /**
     * Gets the objective criteria from the context.
     *
     * @param context the command context
     * @param name    the argument name
     * @return the objective criteria
     */
    public static ObjectiveCriteria getCriteria(CommandContext<ClientCommandSource> context, String name) {
        return context.getArgument(name, ObjectiveCriteria.class);
    }

    /**
     * Parses an objective criteria from the reader, delegating to the vanilla implementation.
     *
     * @param stringReader the string reader
     * @return the parsed objective criteria
     * @throws CommandSyntaxException if the criteria is invalid
     */
    @Override
    public ObjectiveCriteria parse(StringReader stringReader) throws CommandSyntaxException {
        return ObjectiveCriteriaArgument.criteria().parse(stringReader);
    }

    /**
     * Suggests the names of all available criteria, mirroring the vanilla implementation.
     *
     * @param commandContext     the command context
     * @param suggestionsBuilder the suggestions builder
     * @param <S>                the type of the command source
     * @return the suggestions for the current input
     */
    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> commandContext, SuggestionsBuilder suggestionsBuilder) {
        //#if MC >= 1.19.3
        //$$ List<String> list = Lists.newArrayList(ObjectiveCriteria.getCustomCriteriaNames());
        //$$
        //$$ for (StatType<?> statType : BuiltInRegistries.STAT_TYPE) {
        //$$     for (Object object : statType.getRegistry()) {
        //$$         String string = this.getName(statType, object);
        //$$         list.add(string);
        //$$     }
        //$$ }
        //$$
        //$$ return SharedSuggestionProvider.suggest(list, suggestionsBuilder);
        //#elseif MC >= 1.17.1
        //$$ List<String> list = Lists.newArrayList(ObjectiveCriteria.getCustomCriteriaNames());
        //$$
        //$$ for (StatType<?> statType : Registry.STAT_TYPE) {
        //$$     for (Object object : statType.getRegistry()) {
        //$$         String string = this.getName(statType, object);
        //$$         list.add(string);
        //$$     }
        //$$ }
        //$$
        //$$ return SharedSuggestionProvider.suggest(list, suggestionsBuilder);
        //#else
        List<String> list = Lists.newArrayList(ObjectiveCriteria.CRITERIA_BY_NAME.keySet());

        for (StatType<?> statType : Registry.STAT_TYPE) {
            for (Object object : statType.getRegistry()) {
                String string = this.getName(statType, object);
                list.add(string);
            }
        }

        return SharedSuggestionProvider.suggest(list, suggestionsBuilder);
        //#endif
    }

    /**
     * Builds the name of the given stat.
     *
     * @param statType the stat type
     * @param object   the stat object
     * @param <T>      the type of the stat
     * @return the name
     */
    @SuppressWarnings("unchecked")
    public <T> String getName(StatType<T> statType, Object object) {
        return Stat.buildName(statType, (T) object);
    }

    /**
     * Gets the example strings for this argument.
     *
     * @return the example strings
     */
    @Override
    public Collection<String> getExamples() {
        return ClientObjectiveCriteriaArgument.EXAMPLES;
    }
}
