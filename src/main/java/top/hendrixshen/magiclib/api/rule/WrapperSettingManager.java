package top.hendrixshen.magiclib.api.rule;

//#if MC >= 11901
import carpet.api.settings.SettingsManager;
import net.minecraft.commands.CommandBuildContext;
//#else
//#if MC > 11502
//$$ import carpet.settings.Condition;
//#endif
//$$ import carpet.settings.SettingsManager;
//#endif
//#if MC > 11404
import carpet.network.ServerNetworkHandler;
//#endif
import carpet.script.CarpetEventServer;
import carpet.settings.ParsedRule;
import com.google.common.collect.Lists;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
//#if MC <= 11605
//$$ import org.apache.commons.lang3.tuple.Pair;
//#endif
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.MagicLibSettings;
import top.hendrixshen.magiclib.api.rule.annotation.Rule;
import top.hendrixshen.magiclib.compat.minecraft.network.chat.ComponentCompatApi;
import top.hendrixshen.magiclib.language.I18n;
import top.hendrixshen.magiclib.util.MessageUtil;
import top.hendrixshen.magiclib.util.ReflectUtil;

import java.lang.reflect.Field;
//#if MC <= 11802
//$$ import java.util.Map;
//#endif
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class WrapperSettingManager extends SettingsManager {
    private static final ConcurrentHashMap<String, WrapperSettingManager> INSTANCES = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<String, RuleOption> OPTIONS = new ConcurrentHashMap<>();
    private final LinkedBlockingQueue<String> CATEGORIES = new LinkedBlockingQueue<>();
    private final ConcurrentHashMap<ParsedRule<?>, RuleOption> RULE_TO_OPTION = new ConcurrentHashMap<>();
    private final String version;
    private final String fancyName;
    private final String identifier;
    public static final String DEFAULT_LANGUAGE = "en_us";
    private final List<WrapperSettingManager.RuleCallback> callbacks = Lists.newArrayList();
    private static final List<WrapperSettingManager.RuleCallback> globalCallbacks = Lists.newArrayList();
    public WrapperSettingManager(String version, String identifier, String fancyName) {
        super(version, identifier, fancyName);
        this.version = version;
        this.identifier = identifier;
        this.fancyName = fancyName;
    }

    public static WrapperSettingManager get(String identifier) {
        return WrapperSettingManager.INSTANCES.get(identifier);
    }

    public void registerRuleCallback(WrapperSettingManager.RuleCallback callback) {
        this.callbacks.add(callback);// 136
    }

    public static void registerGlobalRuleCallback(WrapperSettingManager.RuleCallback callback) {
        WrapperSettingManager.globalCallbacks.add(callback);// 150
    }

    public void onRuleChange(CommandSourceStack source, @NotNull RuleOption rule, String value) {
        this.callbacks.forEach(ruleCallback -> ruleCallback.callback(source, rule, value));
        WrapperSettingManager.globalCallbacks.forEach(ruleCallback -> ruleCallback.callback(source, rule, value));
        //#if MC > 11404
        ServerNetworkHandler.updateRuleWithConnectedClients(rule.getRule());
        //#endif
        // TODO this.switchScarpetRuleIfNeeded(source, rule);// 266
        //#if MC > 11502
        if (CarpetEventServer.Event.CARPET_RULE_CHANGES.isNeeded()) {
            CarpetEventServer.Event.CARPET_RULE_CHANGES.onCarpetRuleChanges(rule.getRule(), source);
        }
        //#endif
    }

    public static void register(String identifier, @NotNull WrapperSettingManager wrapperSettingsManager) {
        if (wrapperSettingsManager.getClass() == WrapperSettingManager.class) {
            throw new IllegalArgumentException("Forbidden original use of WrapperSettingManager!");
        }

        if (WrapperSettingManager.INSTANCES.containsKey(identifier)) {
            MagicLibReference.LOGGER.error("SettingManager {} is registered", identifier);
        }
        WrapperSettingManager.INSTANCES.put(identifier, wrapperSettingsManager);
    }

    public Collection<RuleOption> getNonDefaultRuleOption() {
        return this.OPTIONS.values().stream().filter(ruleOption -> !ruleOption.isDefault() && ruleOption.isEnabled())
                .sorted(Comparator.comparing(RuleOption::getName)).collect(Collectors.toList());
    }

    // Compat for legacy Minecraft version.
    public static boolean canUseCommand(CommandSourceStack source, Object commandLevel) {
        if (commandLevel instanceof Boolean) {
            return (Boolean)commandLevel;
        } else {
            String commandLevelString = commandLevel.toString();
            switch(commandLevelString) {
                case "true":
                    return true;
                case "ops":
                    return source.hasPermission(2);
                case "0":
                case "1":
                case "2":
                case "3":
                case "4":
                    return source.hasPermission(Integer.parseInt(commandLevelString));
                default:
                    return false;
            }
        }
    }

    //#if MC <= 11802
    //$$ @SuppressWarnings("unchecked")
    //#endif
    @Override
    public void parseSettingsClass(@NotNull Class settingsClass) {
        for (Field field : settingsClass.getDeclaredFields()) {
            Rule rule = field.getAnnotation(Rule.class);
            if (rule == null) {
                continue;
            }

            ParsedRule<?> carpetRule = (ParsedRule<?>) ReflectUtil.newInstance(
                    "carpet.settings.ParsedRule", new Class[]{Field.class, Rule.class, WrapperSettingManager.class},
                    field, rule, this);
            RuleOption ruleOption = new RuleOption(rule, carpetRule);

            for (String category : rule.categories()) {
                if (!this.CATEGORIES.contains(category)) {
                    this.CATEGORIES.add(category);
                }
            }
            //#if MC > 11802
            this.addCarpetRule(carpetRule);
            //#else
            //$$ ((Map<String, ParsedRule<?>>) ReflectUtil.getFieldValue("carpet.settings.SettingsManager", "rules", this)).put(carpetRule.name, carpetRule);
            //#endif
            this.OPTIONS.putIfAbsent(field.getName(), ruleOption);
            this.RULE_TO_OPTION.putIfAbsent(carpetRule, ruleOption);
        }
    }

    //#if MC > 11502
    @Override
    //#if MC > 11802
    public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext commandBuildContext) {
    //#else
    //$$ public void registerCommand(CommandDispatcher<CommandSourceStack> dispatcher) {
    //#endif
        this.registerCommandCompat(dispatcher);
    }
    //#endif

    public void registerCommandCompat(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        if (dispatcher.getRoot().getChildren().stream().anyMatch(node -> node.getName().equalsIgnoreCase(this.identifier))) {
            MagicLibReference.LOGGER.error("Failed to add settings command for {}. It is masking previous command.", this.identifier);
            return;
        }
        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(this.identifier)
                .requires(player -> WrapperSettingManager.canUseCommand(player, this.getSettingsManagerPermissionLevel()))
                .executes(c -> this.displayMainMenu(c.getSource()))
                .then(Commands.literal("list")
                        .executes(c -> this.listAllSettings(c.getSource()))
                        .then(Commands.literal("default")
                                .executes(c -> this.listDefaultSettings(c.getSource())))
                        .then(Commands.argument("tags", StringArgumentType.word())
                                .suggests((c, b) -> SharedSuggestionProvider.suggest(this.getCategories(), b))
                                .executes(c -> this.listTagSettings(c.getSource(), StringArgumentType.getString(c, "tags")))))
                .then(Commands.literal("removeDefault")
                        .requires(s -> !this.locked())
                        .then(Commands.argument("rule", StringArgumentType.word())
                                .suggests((c, b) -> SharedSuggestionProvider.suggest(this.OPTIONS.values().stream()
                                        .filter(RuleOption::isEnabled).map(RuleOption::getName), b))
                                .executes(c -> this.removeDefault(c.getSource(), this.getRuleOption(c)))))
                .then(Commands.literal("setDefault")
                        .requires(s -> !this.locked())
                        .then(Commands.argument("rule", StringArgumentType.word())
                                .suggests((c, b) -> SharedSuggestionProvider.suggest(this.OPTIONS.values().stream()
                                        .filter(RuleOption::isEnabled).map(RuleOption::getName), b))
                                .then(Commands.argument("value", StringArgumentType.greedyString())
                                        .suggests(this::getRuleOptionSuggestion)
                                        .executes(c -> this.setDefault(c.getSource(), this.getRuleOption(c), StringArgumentType.getString(c, "value"))))))
                .then(Commands.argument("rule", StringArgumentType.word())
                        .suggests((c, b) -> SharedSuggestionProvider.suggest(this.OPTIONS.values().stream()
                                .filter(RuleOption::isEnabled).map(RuleOption::getName), b))
                        .then(Commands.argument("value", StringArgumentType.greedyString())
                                .requires(s -> !this.locked())
                                .suggests(this::getRuleOptionSuggestion)
                                .executes(c -> this.setRule(c.getSource(), this.getRuleOption(c), StringArgumentType.getString(c, "value"))))
                        .executes(c -> this.displayRuleMenu(c.getSource(), this.getRuleOption(c))));
        dispatcher.register(command);
    }

    private @Nullable CompletableFuture<Suggestions> getRuleOptionSuggestion(CommandContext<CommandSourceStack> ctx,
                                                                             SuggestionsBuilder suggestionsBuilder) throws CommandSyntaxException {
        RuleOption ruleOption = this.getRuleOption(ctx);
        return ruleOption.isEnabled() ? SharedSuggestionProvider.suggest(this.getRuleOption(ctx)
                .getOptions().stream().sorted().collect(Collectors.toList()), suggestionsBuilder) : null;
    }

    @Override
    public Iterable<String> getCategories() {
        return this.CATEGORIES;
    }

    public Collection<RuleOption> getRuleOptions() {
        return this.OPTIONS.values();
    }

    public RuleOption getRuleOption(String name) {
        return this.OPTIONS.get(name);
    }

    public RuleOption getRuleOption(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String ruleName = StringArgumentType.getString(ctx, "rule");
        RuleOption ruleOption = this.getRuleOption(ruleName);
        if (ruleOption == null) {
            throw new SimpleCommandExceptionType(ComponentCompatApi.literal(this.trUI("unknown_rule", ruleName))
                    .withStyle(style -> style.withBold(true).withColor(ChatFormatting.RED))).create();
        }
        return ruleOption;
    }

    public RuleOption getRuleOption(ParsedRule<?> carpetRule) {
        return this.RULE_TO_OPTION.get(carpetRule);
    }

    public String getSettingsManagerPermissionLevel() {
        return MagicLibSettings.settingManagerLevel;
    }

    public String getCurrentLanguageCode() {
        return MagicLibSettings.language;
    }

    public String getTranslatedRuleName(String ruleName) {
        return (!this.getCurrentLanguageCode().equals(WrapperSettingManager.DEFAULT_LANGUAGE) &&
                I18n.exists(this.getCurrentLanguageCode(), String.format("%s.rule.%s.name", this.identifier, ruleName))) ?
                String.format("%s (%s)", this.trRuleName(ruleName), this.defaultRuleName(ruleName)) :
                this.defaultRuleName(ruleName);
    }

    public String trFancyName() {
        return I18n.exists(WrapperSettingManager.DEFAULT_LANGUAGE, String.format("%s.info.mod_name", this.identifier)) ?
                this.trInfo("mod_name") : this.fancyName;
    }

    public String getIdentifier() {
        return this.identifier;
    }

    public String getVersion() {
        return this.version;
    }

    //#if MC <= 11802
    //$$ public boolean locked() {
    //$$     return this.locked;
    //$$ }
    //#endif

    // The server side requires this way to use the fallback language.
    public String tr(String code, String key, Object... objects) {
        String value;
        return (value = I18n.getByCode(code, key, objects)).equals(key) ? I18n.getByCode(WrapperSettingManager.DEFAULT_LANGUAGE, key, objects) : value;
    }

    // The server side requires this way to use the fallback language.
    public String tr(String code, String key) {
        String value;
        return (value = I18n.getByCode(code, key)).equals(key) ? I18n.getByCode(WrapperSettingManager.DEFAULT_LANGUAGE, key) : value;
    }

    public String defaultRuleName(String ruleName) {
        return I18n.exists(WrapperSettingManager.DEFAULT_LANGUAGE, String.format("%s.rule.%s.name", this.identifier, ruleName)) ?
                this.tr(WrapperSettingManager.DEFAULT_LANGUAGE, String.format("%s.rule.%s.name", this.identifier, ruleName)) : ruleName;
    }

    public String trRuleName(String ruleName) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.rule.%s.name", this.identifier, ruleName));
    }

    public String trRuleDesc(String ruleName) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.rule.%s.desc", this.identifier, ruleName));
    }

    public String trCategory(String category) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.category.%s", this.identifier, category));
    }

    public String trInfo(String info) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.info.%s", this.identifier, info));
    }

    public String trValidator(String uiText) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.validator.%s", MagicLibReference.getModId(), uiText));
    }

    public String trValidator(String uiText, Object... objects) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.validator.%s", MagicLibReference.getModId(), uiText), objects);
    }

    public String trUI(String uiText) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.ui.%s", MagicLibReference.getModId(), uiText));
    }

    public String trUI(String uiText, Object... objects) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.ui.%s", MagicLibReference.getModId(), uiText), objects);
    }

    public List<Component> trRuleExtraInfo(String ruleName) {
        List<Component> ret = Lists.newArrayList();
        String key = String.format("%s.rule.%s.extra.%%d", this.identifier, ruleName);
        for (int i = 0; I18n.exists(String.format(key, i)); i++) {
            ret.add(ComponentCompatApi.literal(this.tr(this.getCurrentLanguageCode(), String.format(key, i))));
        }
        return ret;
    }

    public int displayRuleMenu(@NotNull CommandSourceStack source, @NotNull RuleOption ruleOption) {
        if (!ruleOption.isEnabled()) {
            MessageUtil.sendMessage(source, trUI("disabled", ruleOption.getName()));
            return 1;
        }

        MessageUtil.sendServerMessage(source.getServer(), "");
        MessageUtil.sendServerMessage(source.getServer(), ComponentCompatApi.literal(this.getTranslatedRuleName(ruleOption.getName())).withStyle(
                style -> style.withBold(true)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/%s %s", this.identifier, ruleOption.getName())))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentCompatApi.literal(this.trUI("hover.refresh"))
                                .withStyle(style1 -> style1.withColor(ChatFormatting.GRAY))))));
        MessageUtil.sendServerMessage(source.getServer(), this.trRuleDesc(ruleOption.getName()));
        for (Component component : this.trRuleExtraInfo(ruleOption.getName())) {
            MessageUtil.sendServerMessage(source.getServer(), component.copy().withStyle(style -> style.withColor(ChatFormatting.GRAY)));
        }
        List<Component> categories = Lists.newArrayList();
        categories.add(ComponentCompatApi.literal(this.trUI("tags")));
        for (String category : ruleOption.getCategory()) {
            categories.add(ComponentCompatApi.literal(String.format("[%s]", this.trCategory(category))).withStyle(style ->
                    style.withColor(ChatFormatting.AQUA)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/%s list %s", this.identifier, category)))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentCompatApi.literal(this.trUI("hover.list_all_category", category))))));
            categories.add(ComponentCompatApi.literal(" "));
        }
        if (categories.size() > 1) {
            categories.remove(categories.size() - 1);
        } else {
            categories.add(ComponentCompatApi.literal(this.trUI("null")).withStyle(style -> style.withColor(ChatFormatting.GRAY)));
        }
        MessageUtil.sendServerMessage(source.getServer(), categories);
        List<Component> value = Lists.newArrayList();
        value.add(ComponentCompatApi.literal(this.trUI("current_value")));
        value.add(ComponentCompatApi.literal(String.format("%s (%s)", ruleOption.getValue(), ruleOption.isDefault() ?
                this.trUI("default") : this.trUI("modified"))).withStyle(
                        style -> style.withBold(true)
                                .withColor(ruleOption.isDefault() ? ChatFormatting.DARK_RED : ChatFormatting.GREEN)));
        MessageUtil.sendServerMessage(source.getServer(), value);
        List<Component> options = Lists.newArrayList();
        options.add(ComponentCompatApi.literal(this.trUI("options")));
        options.add(ComponentCompatApi.literal("[ ").withStyle(style -> style.withColor(ChatFormatting.YELLOW)));
        for (String option : ruleOption.getOptions()) {
            options.add(ComponentCompatApi.literal(option).withStyle(style ->
                    style.withUnderlinedCompat(ruleOption.getStringValue().equals(option))
                            .withColor(ruleOption.isDefault() ? ChatFormatting.GRAY : ruleOption.getDefaultStringValue().equals(option) ? ChatFormatting.DARK_GREEN : ChatFormatting.YELLOW)
                            .withClickEvent(ruleOption.getStringValue().equals(option) || this.locked() ? null : new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/%s %s %s", this.identifier, ruleOption.getName(), option)))
                            .withHoverEvent(ruleOption.getStringValue().equals(option) || this.locked() ? null : new HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentCompatApi.literal(this.trUI("hover.switch_to", option))))));
            options.add(ComponentCompatApi.literal(" "));
        }
        if (options.size() > 2) {
            options.remove(options.size() - 1);
        }
        options.add(ComponentCompatApi.literal(" ]").withStyle(style -> style.withColor(ChatFormatting.YELLOW)));
        MessageUtil.sendServerMessage(source.getServer(), options);
        return 1;
    }

    public int setRule(CommandSourceStack source, @NotNull RuleOption ruleOption, String newValue) {
        if (!ruleOption.isEnabled()) {
            MessageUtil.sendMessage(source, trUI("disabled", ruleOption.getName()));
            return 1;
        }

        if (ruleOption.setValue(source, newValue) == null) {
            return 1;
        }

        List<Component> components = Lists.newArrayList();
        components.add(ComponentCompatApi.literal(String.format("%s: %s, ", this.getTranslatedRuleName(ruleOption.getName()), newValue)));
        components.add(ComponentCompatApi.literal(String.format("[%s]", this.trUI("change_permanently")))
                .withStyle(style -> style.withColor(ChatFormatting.AQUA)
                        .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/%s setDefault %s %s", this.identifier, ruleOption.getName(), newValue)))
                        .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentCompatApi.literal(this.trUI("hover.change_permanently", String.format("%s.conf",this.identifier)))))));
        MessageUtil.sendMessage(source, components);
        return 1;
    }

    @SuppressWarnings("unchecked")
    public int setDefault(CommandSourceStack source, RuleOption ruleOption, String newValue) {
        if (this.locked()) {
            MessageUtil.sendMessage(source, trUI("locked"));
            return 1;
        }

        if (!ruleOption.isEnabled()) {
            MessageUtil.sendMessage(source, trUI("disabled", ruleOption.getName()));
            return 1;
        }

        if (ruleOption.setValue(source, newValue) == null) {
            return 1;
        }

        Class<?> carpetSM = WrapperSettingManager.class.getSuperclass();
        //#if MC > 11502
        Object path = ReflectUtil.invokeDeclared(carpetSM, "getFile", this);
        Object conf = ReflectUtil.invokeDeclared(carpetSM, "readSettingsFromConf", this, new Class<?>[]{Path.class}, path);
        //#else
        //$$ Object conf = ReflectUtil.invokeDeclared(carpetSM, "readSettingsFromConf", this);
        //#endif
        //#if MC > 11605
        Map<String, String> ruleMap = (Map<String, String>) ReflectUtil.invoke(ReflectUtil.getInnerClass(carpetSM,
                "ConfigReadResult"), "ruleMap", conf);
        //#else
        //$$ Map<String, String> ruleMap = ((Pair<Map<String, String>, Boolean>) conf).getLeft();
        //#endif
        ruleMap.put(ruleOption.getName(), newValue);
        //#if MC > 11802
        ReflectUtil.invokeDeclared(carpetSM, "writeSettingsToConf", this,
                new Class[]{ReflectUtil.getInnerClass(carpetSM, "ConfigReadResult")}, conf);
        //#else
        //$$ReflectUtil.invokeDeclared(carpetSM, "writeSettingsToConf", this,
        //$$        new Class[]{Map.class}, ruleMap);
        //#endif
        MessageUtil.sendMessage(source, ComponentCompatApi.literal(this.trUI("set_default", this.getTranslatedRuleName(ruleOption.getName()),
                ruleOption.getStringValue())).withStyle(style -> style.withItalic(true).withColor(ChatFormatting.GRAY)));
        return 1;
    }

    @SuppressWarnings("unchecked")
    public int removeDefault(CommandSourceStack source, RuleOption ruleOption) {
        if (this.locked()) {
            MessageUtil.sendMessage(source, trUI("locked"));
            return 1;
        }

        if (!ruleOption.isEnabled()) {
            MessageUtil.sendMessage(source, trUI("disabled", ruleOption.getName()));
            return 1;
        }

        if (ruleOption.resetValue(source) == null) {
            return 1;
        }

        Class<?> carpetSM = WrapperSettingManager.class.getSuperclass();
        //#if MC > 11502
        Object path = ReflectUtil.invokeDeclared(carpetSM, "getFile", this);
        Object conf = ReflectUtil.invokeDeclared(carpetSM, "readSettingsFromConf", this, new Class<?>[]{Path.class}, path);
        //#else
        //$$ Object conf = ReflectUtil.invokeDeclared(carpetSM, "readSettingsFromConf", this);
        //#endif
        //#if MC > 11605
        Map<String, String> ruleMap = (Map<String, String>) ReflectUtil.invoke(ReflectUtil.getInnerClass(carpetSM,
                "ConfigReadResult"), "ruleMap", conf);
        //#else
        //$$ Map<String, String> ruleMap = ((Pair<Map<String, String>, Boolean>) conf).getLeft();
        //#endif
        ruleMap.remove(ruleOption.getName());
        //#if MC > 11802
        ReflectUtil.invokeDeclared(carpetSM, "writeSettingsToConf", this,
                new Class[]{ReflectUtil.getInnerClass(carpetSM, "ConfigReadResult")}, conf);
        //#else
        //$$ReflectUtil.invokeDeclared(carpetSM, "writeSettingsToConf", this,
        //$$        new Class[]{Map.class}, ruleMap);
        //#endif
        MessageUtil.sendMessage(source, ComponentCompatApi.literal(this.trUI("reset_default", this.getTranslatedRuleName(ruleOption.getName())))
                .withStyle(style -> style.withItalic(true).withColor(ChatFormatting.GRAY)));
        return 1;
    }

    public Collection<List<Component>> getMatchedSettings(@NotNull Collection<RuleOption> ruleOptions) {
        ruleOptions = ruleOptions.stream().filter((RuleOption::isEnabled)).collect(Collectors.toList());
        Collection<List<Component>> collection = Lists.newArrayList();
        for (RuleOption ruleOption : ruleOptions) {
            List<Component> components = Lists.newArrayList();
            components.add(ComponentCompatApi.literal(String.format("- %s", this.getTranslatedRuleName(ruleOption.getName()))).withStyle(style -> style
                    .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/%s %s", this.identifier, ruleOption.getName())))
                    .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentCompatApi.literal(this.trRuleDesc(ruleOption.getName())).withStyle(style1 -> style1.withColor(ChatFormatting.YELLOW))))));
            components.add(ComponentCompatApi.literal(" "));
            List<String> options = new ArrayList<>(ruleOption.getOptions());
            if (!options.contains(ruleOption.getStringValue())) {
                options.add(ruleOption.getStringValue());
            }
            for (String option : options) {
                components.add(ComponentCompatApi.literal(String.format("[%s]", option)).withStyle(style ->
                        style.withUnderlinedCompat(ruleOption.getStringValue().equals(option))
                                .withColor(ruleOption.isDefault() ? ChatFormatting.GRAY : ruleOption.getDefaultStringValue().equals(option) ? ChatFormatting.DARK_GREEN : ChatFormatting.YELLOW)
                                .withClickEvent(ruleOption.getStringValue().equals(option) || this.locked() ? null : new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, String.format("/%s %s %s", this.identifier, ruleOption.getName(), option)))
                                .withHoverEvent(ruleOption.getStringValue().equals(option) || this.locked() ? null : new HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentCompatApi.literal(this.trUI("hover.switch_to", option))))));
                components.add(ComponentCompatApi.literal(" "));
            }
            components.remove(components.size() - 1);
            collection.add(components);
        }
        return collection;
    }

    private int listTagSettings(CommandSourceStack source, String category) {
        MessageUtil.sendMessage(source, ComponentCompatApi.literal(this.trUI("matched_rule",  this.trFancyName(), category))
                .withStyle(style -> style.withBold(true)));
        for (List<Component> item : this.getMatchedSettings(this.OPTIONS.values().stream().filter(ruleOption ->
                Arrays.stream(ruleOption.getCategory()).collect(Collectors.toList()).contains(category)).collect(Collectors.toList()))) {
            MessageUtil.sendMessage(source, item);
        }
        return 1;
    }

    private int displayMainMenu(CommandSourceStack source) {
        MessageUtil.sendMessage(source, ComponentCompatApi.literal(this.trUI("current_rule",  this.trFancyName()))
                .withStyle(style -> style.withBold(true)));
        for (List<Component> item : this.getMatchedSettings(this.getNonDefaultRuleOption())) {
            MessageUtil.sendMessage(source, item);
        }
        for (WrapperSettingManager instance : WrapperSettingManager.INSTANCES.values()) {
            MessageUtil.sendMessage(source, ComponentCompatApi.literal(this.trUI("version", instance.trFancyName(), instance.getVersion()))
                    .withStyle(style -> style.withColor(ChatFormatting.GRAY)));
        }
        MessageUtil.sendMessage(source, ComponentCompatApi.literal(this.trUI("browse",  this.trFancyName()))
                .withStyle(style -> style.withBold(true)));
        List<Component> categories = Lists.newArrayList();
        categories.add(ComponentCompatApi.literal(this.trUI("tags")));
        for (String category : this.CATEGORIES) {
            categories.add(ComponentCompatApi.literal(String.format("[%s]", this.trCategory(category))).withStyle(style ->
                    style.withColor(ChatFormatting.AQUA)
                            .withClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/%s list %s", this.identifier, category)))
                            .withHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, ComponentCompatApi.literal(this.trUI("hover.list_all_category", category))))));
            categories.add(ComponentCompatApi.literal(" "));
        }
        if (categories.size() > 1) {
            categories.remove(categories.size() - 1);
        } else {
            categories.add(ComponentCompatApi.literal(this.trUI("null")).withStyle(style -> style.withColor(ChatFormatting.GRAY)));
        }
        MessageUtil.sendServerMessage(source.getServer(), categories);
        return 1;
    }

    @SuppressWarnings("unchecked")
    private int listDefaultSettings(CommandSourceStack source) {
        MessageUtil.sendMessage(source, ComponentCompatApi.literal(this.trUI("default_rule",  this.trFancyName(), String.format("%s.conf", this.identifier)))
                .withStyle(style -> style.withBold(true)));
        Class<?> carpetSM = WrapperSettingManager.class.getSuperclass();
        //#if MC > 11502
        Object path = ReflectUtil.invokeDeclared(carpetSM, "getFile", this);
        Object conf = ReflectUtil.invokeDeclared(carpetSM, "readSettingsFromConf", this, new Class<?>[]{Path.class}, path);
        //#else
        //$$ Object conf = ReflectUtil.invokeDeclared(carpetSM, "readSettingsFromConf", this);
        //#endif
        //#if MC > 11605
        Set<String> defaults = ((Map<String, String>) ReflectUtil.invoke(ReflectUtil.getInnerClass(carpetSM,
                "ConfigReadResult"), "ruleMap", conf)).keySet();
        //#else
        //$$ Set<String> defaults = ((Pair<Map<String, String>, Boolean>) conf).getLeft().keySet();
        //#endif
        for (List<Component> item : this.getMatchedSettings(this.OPTIONS.values().stream().filter(ruleOption ->
                defaults.contains(ruleOption.getName())).collect(Collectors.toList()))) {
            MessageUtil.sendMessage(source, item);
        }
        return 1;
    }

    public int listAllSettings(CommandSourceStack source) {
        MessageUtil.sendMessage(source, ComponentCompatApi.literal(this.trUI("all_rule",  this.trFancyName()))
                .withStyle(style -> style.withBold(true)));
        for (List<Component> item : this.getMatchedSettings(this.OPTIONS.values())) {
            MessageUtil.sendMessage(source, item);
        }
        return 1;
    }

    @FunctionalInterface
    public interface RuleCallback {
        void callback(CommandSourceStack source, RuleOption rule, String value);
    }
}
