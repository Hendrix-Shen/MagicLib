package top.hendrixshen.magiclib.carpet.impl;

import carpet.CarpetServer;
import carpet.settings.ParsedRule;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Queues;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import lombok.Getter;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.HoverEvent;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ClickEventCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.HoverEventCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.MutableComponentCompat;
import top.hendrixshen.magiclib.api.fake.i18n.ServerPlayerLanguage;
import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.carpet.api.CarpetExtensionCompatApi;
import top.hendrixshen.magiclib.carpet.api.annotation.Rule;
import top.hendrixshen.magiclib.impl.i18n.minecraft.translation.HookTranslationManager;
import top.hendrixshen.magiclib.mixin.carpet.accessor.SettingsManagerAccessor;
import top.hendrixshen.magiclib.impl.carpet.MagicLibSettings;
import top.hendrixshen.magiclib.util.ReflectionUtil;
import top.hendrixshen.magiclib.util.collect.Provider;
import top.hendrixshen.magiclib.util.minecraft.ComponentUtil;
import top.hendrixshen.magiclib.util.minecraft.MessageUtil;

import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

//#if MC > 11903
//$$ import carpet.api.settings.CarpetRule;
//#endif

//#if MC > 11900
//$$ import carpet.api.settings.SettingsManager;
//$$ import net.minecraft.commands.CommandBuildContext;
//#else
import carpet.settings.SettingsManager;

import java.util.Map;
//#endif

//#if MC < 11700
import org.apache.commons.lang3.tuple.Pair;
//#endif

//#if MC > 11502 && MC < 11904
import carpet.script.CarpetEventServer;
//#endif

//#if MC > 11404
import carpet.network.ServerNetworkHandler;
//#endif

//#if MC > 11900
//$$ @SuppressWarnings("removal")
//#endif
public class WrappedSettingManager extends SettingsManager {
    private static final Map<String, WrappedSettingManager> INSTANCES = Maps.newConcurrentMap();
    private final Map<String, RuleOption> OPTIONS = Maps.newConcurrentMap();
    private final Queue<String> CATEGORIES = Queues.newLinkedBlockingDeque();
    private final Map<ParsedRule<?>, RuleOption> RULE_TO_OPTION = Maps.newConcurrentMap();
    @Getter
    private final String version;
    private final String fancyName;
    @Getter
    private final String identifier;
    public static final String DEFAULT_LANGUAGE = "en_us";
    private final List<RuleCallback> callbacks = Lists.newArrayList();
    private static final List<RuleCallback> globalCallbacks = Lists.newArrayList();

    public WrappedSettingManager(String version, String identifier, String fancyName) {
        super(version, identifier, fancyName);
        this.version = version;
        this.identifier = identifier;
        this.fancyName = fancyName;
    }

    public static WrappedSettingManager get(String identifier) {
        return WrappedSettingManager.INSTANCES.get(identifier);
    }

    public static void printAllExtensionVersion(CommandSourceStack source) {
        WrappedSettingManager.INSTANCES.values().forEach(instance ->
                MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.version",
                                instance.getTranslatedFancyName(), instance.getVersion())
                        .withStyle(style -> style.withColor(ChatFormatting.GRAY))));
    }

    public void registerRuleCallback(RuleCallback callback) {
        this.callbacks.add(callback);
    }

    public static void registerGlobalRuleCallback(RuleCallback callback) {
        WrappedSettingManager.globalCallbacks.add(callback);
    }

    public void onRuleChange(CommandSourceStack source, @NotNull RuleOption rule, String value) {
        this.callbacks.forEach(ruleCallback -> ruleCallback.callback(source, rule, value));
        WrappedSettingManager.globalCallbacks.forEach(ruleCallback -> ruleCallback.callback(source, rule, value));

        //#if MC > 11404
        ServerNetworkHandler.updateRuleWithConnectedClients(rule.getRule());
        //#endif

        //#if MC > 11903
        //$$ ReflectionUtil.invokeDeclared("carpet.api.settings.SettingsManager",
        //$$         "switchScarpetRuleIfNeeded", this,
        //$$         new Class[]{CommandSourceStack.class, CarpetRule.class}, source, rule.getRule());
        //#elseif MC > 11502
        if (CarpetEventServer.Event.CARPET_RULE_CHANGES.isNeeded()) {
            CarpetEventServer.Event.CARPET_RULE_CHANGES.onCarpetRuleChanges(rule.getRule(), source);
        }
        //#endif
    }

    public static <T extends WrappedSettingManager> void register(String identifier, T wrapperSettingsManager,
                                                                  CarpetExtensionCompatApi extension) {
        if (WrappedSettingManager.INSTANCES.containsKey(identifier)) {
            MagicLib.getLogger().error("SettingManager {} is registered", identifier);
        }

        CarpetServer.manageExtension(extension);
        WrappedSettingManager.INSTANCES.put(identifier, wrapperSettingsManager);
        HookTranslationManager.getInstance().registerNamespace(identifier);
    }

    public Collection<RuleOption> getNonDefaultRuleOption() {
        return this.OPTIONS.values().stream()
                .filter(ruleOption -> !ruleOption.isDefault() && ruleOption.isEnabled())
                .sorted(Comparator.comparing(RuleOption::getName))
                .collect(Collectors.toList());
    }

    // Compat for legacy Minecraft version.
    public static boolean canUseCommand(CommandSourceStack source, Object commandLevel) {
        if (commandLevel instanceof Boolean) {
            return (Boolean) commandLevel;
        } else {
            String commandLevelString = commandLevel.toString();

            switch (commandLevelString) {
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

    @Override
    public void parseSettingsClass(@NotNull Class settingsClass) {
        for (Field field : settingsClass.getDeclaredFields()) {
            Rule rule = field.getAnnotation(Rule.class);

            if (rule == null) {
                continue;
            }

            ReflectionUtil.newInstance("carpet.settings.ParsedRule",
                            new Class[]{Field.class, Rule.class, WrappedSettingManager.class},
                            field, rule, this)
                    .ifPresent(o -> {
                        ParsedRule<?> carpetRule = (ParsedRule<?>) o;
                        RuleOption ruleOption = new RuleOption(rule, carpetRule);

                        for (String category : rule.categories()) {
                            if (!this.CATEGORIES.contains(category)) {
                                this.CATEGORIES.add(category);
                            }
                        }

                        //#if MC > 11802
                        //$$ this.addCarpetRule(carpetRule);
                        //#else
                        ((SettingsManagerAccessor) this).getRules().put(carpetRule.name, carpetRule);
                        //#endif
                        this.OPTIONS.putIfAbsent(field.getName(), ruleOption);
                        this.RULE_TO_OPTION.putIfAbsent(carpetRule, ruleOption);
                    });
        }
    }

    //#if MC > 11502
    @Override
    public void registerCommand(
            CommandDispatcher<CommandSourceStack> dispatcher
            //#if MC > 11802
            //$$ , CommandBuildContext commandBuildContext
            //#endif
    ) {
        this.registerCommandCompat(dispatcher);
    }
    //#endif

    public void registerCommandCompat(@NotNull CommandDispatcher<CommandSourceStack> dispatcher) {
        if (dispatcher.getRoot().getChildren().stream()
                .anyMatch(node -> node.getName().equalsIgnoreCase(this.identifier))) {
            MagicLib.getLogger().error("Failed to add settings command for {}. It is masking previous command.",
                    this.identifier);
            return;
        }

        LiteralArgumentBuilder<CommandSourceStack> command = Commands.literal(this.identifier)
                .requires(player -> WrappedSettingManager.canUseCommand(player, this.getSettingsManagerPermissionLevel()))
                .executes(c -> this.displayMainMenu(c.getSource()))
                .then(Commands.literal("list")
                        .executes(c -> this.listAllSettings(c.getSource()))
                        .then(Commands.literal("default")
                                .executes(c -> this.listDefaultSettings(c.getSource())))
                        .then(Commands.argument("tags", StringArgumentType.word())
                                .suggests((c, b) -> SharedSuggestionProvider.suggest(this.getCategories().stream()
                                        .filter(category -> this.getRuleOptionByCategory(category).stream().anyMatch(RuleOption::isEnabled)), b))
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
                        .executes(c -> this.displayRuleMenu(c.getSource(), this.getRuleOption(c))))
                .then(Commands.literal("search")
                        .then(Commands.argument("search", StringArgumentType.greedyString())
                                .executes(c -> this.searchRule(c.getSource(), StringArgumentType.getString(c, "search")))));
        dispatcher.register(command);
    }

    private @Nullable CompletableFuture<Suggestions> getRuleOptionSuggestion(CommandContext<CommandSourceStack> ctx,
                                                                             SuggestionsBuilder suggestionsBuilder)
            throws CommandSyntaxException {
        RuleOption ruleOption = this.getRuleOption(ctx);
        return ruleOption.isEnabled() ? SharedSuggestionProvider.suggest(this.getRuleOption(ctx)
                .getOptions().stream()
                .sorted()
                .collect(Collectors.toList()), suggestionsBuilder) : null;
    }

    @Override
    public Collection<String> getCategories() {
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
            throw new SimpleCommandExceptionType(ComponentUtil.tr("magiclib.ui.unknown_rule", ruleName)
                    .withStyle(style -> style.withBold(true).withColor(ChatFormatting.RED)).get()).create();
        }

        return ruleOption;
    }

    public RuleOption getRuleOption(ParsedRule<?> carpetRule) {
        return this.RULE_TO_OPTION.get(carpetRule);
    }

    public Collection<RuleOption> getRuleOptionByCategory(String category) {
        return this.OPTIONS
                .values()
                .stream()
                .filter(ruleOption -> Arrays.asList(ruleOption.getCategory()).contains(category))
                .collect(Collectors.toList());
    }

    public String getSettingsManagerPermissionLevel() {
        return MagicLibSettings.settingManagerLevel;
    }

    public String getCurrentLanguageCode() {
        return MagicLibSettings.language;
    }

    public String getTranslatedRuleName(@NotNull CommandSourceStack source, String ruleName) {
        String languageCode;

        if (source.getEntity() instanceof ServerPlayer) {
            languageCode = ((ServerPlayerLanguage) source.getEntity()).magicLib$getLanguage();
        } else {
            languageCode = I18n.getCurrentLanguageCode();
        }

        String translatedName = I18n.translateByCodeOrFallback(languageCode,
                String.format("%s.rule.%s.name", this.identifier, ruleName), ruleName);
        String defaultName = this.defaultRuleName(ruleName);
        return defaultName.equals(translatedName) ? defaultName : String.format("%s (%s)", translatedName, defaultName);
    }

    @Deprecated
    public String getTranslatedRuleName(String ruleName) {
        return (!this.getCurrentLanguageCode().equals(WrappedSettingManager.DEFAULT_LANGUAGE) &&
                I18n.exists(this.getCurrentLanguageCode(), String.format("%s.rule.%s.name", this.identifier, ruleName))) ?
                String.format("%s (%s)", this.trRuleName(ruleName), this.defaultRuleName(ruleName)) :
                this.defaultRuleName(ruleName);
    }

    public MutableComponentCompat getTranslatedFancyName() {
        String translationKey = String.format("%s.info.mod_name", this.identifier);

        if (I18n.exists(translationKey)) {
            return ComponentUtil.tr(translationKey);
        }

        return ComponentUtil.simple(this.fancyName);
    }

    public String trFancyName() {
        return I18n.exists(WrappedSettingManager.DEFAULT_LANGUAGE, String.format("%s.info.mod_name", this.identifier)) ?
                this.trInfo("mod_name") : this.fancyName;
    }

    //#if MC < 11900
    public boolean locked() {
        return this.locked;
    }
    //#endif

    public String defaultRuleName(String ruleName) {
        return I18n.translateInCodeOrFallback(WrappedSettingManager.DEFAULT_LANGUAGE,
                String.format("%s.rule.%s.name", this.identifier, ruleName), ruleName);
    }

    @Deprecated
    public String tr(String code, String key, Object... objects) {
        return I18n.trByCode(code, key, objects);
    }

    @Deprecated
    public String tr(String code, String key) {
        return I18n.trByCode(code, key);
    }

    @Deprecated
    public String trRuleName(String ruleName) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.rule.%s.name", this.identifier, ruleName));
    }

    @Deprecated
    public String trRuleDesc(String ruleName) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.rule.%s.desc", this.identifier, ruleName));
    }

    @Deprecated
    public String trCategory(String category) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.category.%s", this.identifier, category));
    }

    @Deprecated
    public String trInfo(String info) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.info.%s", this.identifier, info));
    }

    @Deprecated
    public String trValidator(String uiText) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.validator.%s", MagicLibReference.getModIdentifier(), uiText));
    }

    @Deprecated
    public String trValidator(String uiText, Object... objects) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.validator.%s", MagicLibReference.getModIdentifier(), uiText), objects);
    }

    @Deprecated
    public String trUI(String uiText) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.ui.%s", MagicLibReference.getModIdentifier(), uiText));
    }

    @Deprecated
    public String trUI(String uiText, Object... objects) {
        return this.tr(this.getCurrentLanguageCode(), String.format("%s.ui.%s", MagicLibReference.getModIdentifier(), uiText), objects);
    }

    @Deprecated
    public List<Component> trRuleExtraInfo(String ruleName) {
        List<ComponentCompat> ret = Lists.newArrayList();
        String key = String.format("%s.rule.%s.extra.%%d", this.identifier, ruleName);

        for (int i = 0; I18n.exists(String.format(key, i)); i++) {
            ret.add(ComponentCompat.literal(this.tr(this.getCurrentLanguageCode(), String.format(key, i))));
        }

        return ret.stream().map(Provider::get).collect(Collectors.toList());
    }

    public MutableComponentCompat getTranslatedRuleExtraInfo(String ruleName) {
        List<ComponentCompat> ret = Lists.newArrayList();
        String key = String.format("%s.rule.%s.extra.%%d", this.identifier, ruleName);

        for (int i = 0; I18n.exists(String.format(key, i)); i++) {
            ret.add(ComponentUtil.tr(String.format(key, i)));
        }

        return ComponentUtil.join(ComponentUtil.newLine(), ret);
    }

    public int displayRuleMenu(@NotNull CommandSourceStack source, @NotNull RuleOption ruleOption) {
        if (!ruleOption.isEnabled()) {
            MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.disabled", ruleOption.getName()));
            return 1;
        }

        MessageUtil.sendMessageCompat(source, ComponentUtil.empty());
        MessageUtil.sendMessageCompat(source,
                ComponentUtil.simple(this.getTranslatedRuleName(source, ruleOption.getName()))
                        .withStyle(style -> style
                                .withBold(true)
                                .withClickEvent(ClickEventCompat.of(ClickEvent.Action.RUN_COMMAND,
                                        String.format("/%s %s", this.identifier, ruleOption.getName())))
                                .withHoverEvent(HoverEventCompat.of(HoverEvent.Action.SHOW_TEXT,
                                        ComponentUtil.tr("magiclib.ui.hover.refresh")
                                                .withStyle(style1 -> style1.withColor(ChatFormatting.GRAY))))));

        String descTranslationKey = String.format("%s.rule.%s.desc", this.identifier, ruleOption.getName());

        if (I18n.exists(descTranslationKey)) {
            MessageUtil.sendMessageCompat(source, ComponentUtil.tr(descTranslationKey));
        }

        MessageUtil.sendMessageCompat(source, this.getTranslatedRuleExtraInfo(ruleOption.getName()));
        List<ComponentCompat> categories = Lists.newArrayList();
        categories.add(ComponentUtil.tr("magiclib.ui.tags"));
        categories.add(ComponentUtil.join(ComponentUtil.simple(" "), Arrays.stream(ruleOption.getCategory())
                .sorted()
                .map(category -> ComponentUtil.compose(
                        ComponentUtil.simple("["),
                        ComponentUtil.tr(String.format("%s.category.%s", this.identifier, category)),
                        ComponentUtil.simple("]")).withStyle(style -> style
                        .withColor(ChatFormatting.AQUA)
                        .withClickEvent(ClickEventCompat.of(ClickEvent.Action.RUN_COMMAND,
                                String.format("/%s list %s", this.identifier, category)))
                        .withHoverEvent(HoverEventCompat.of(HoverEvent.Action.SHOW_TEXT,
                                ComponentUtil.tr("magiclib.ui.hover.list_all_category", category)))))
                .collect(Collectors.toList())));

        if (categories.size() < 2) {
            categories.add(ComponentUtil.tr("magiclib.ui.null")
                    .withStyle(style -> style.withColor(ChatFormatting.GRAY)));
        }

        MessageUtil.sendMessageCompat(source, ComponentUtil.join(ComponentUtil.empty(), categories));
        MessageUtil.sendMessageCompat(source, ComponentUtil.compose(
                ComponentUtil.tr("magiclib.ui.current_value"),
                ComponentUtil.compose(
                                ruleOption.getValue(),
                                " (",
                                ComponentUtil.tr(ruleOption.isDefault() ? "magiclib.ui.default" : "magiclib.ui.modified"),
                                ")")
                        .withStyle(style -> style
                                .withBold(true)
                                .withColor(ruleOption.isDefault() ? ChatFormatting.DARK_RED : ChatFormatting.GREEN))
        ));
        MessageUtil.sendMessageCompat(source, ComponentUtil.compose(
                ComponentUtil.tr("magiclib.ui.options"),
                ComponentUtil.simple("[ ").withStyle(style -> style.withColor(ChatFormatting.YELLOW)),
                ComponentUtil.join(ComponentUtil.simple(" "), ruleOption.getOptions().stream()
                        .map(option -> ComponentUtil.simple(option).withStyle(style -> style
                                .withUnderlined(ruleOption.getStringValue().equals(option))
                                .withColor(ruleOption.isDefault() ? ChatFormatting.GRAY :
                                        ruleOption.getDefaultStringValue().equals(option) ? ChatFormatting.DARK_GREEN :
                                                ChatFormatting.YELLOW)
                                .withClickEvent(ruleOption.getStringValue().equals(option) || this.locked() ? null :
                                        ClickEventCompat.of(ClickEvent.Action.SUGGEST_COMMAND,
                                                String.format("/%s %s %s", this.identifier, ruleOption.getName(), option)))
                                .withHoverEvent(ruleOption.getStringValue().equals(option) || this.locked() ? null :
                                        HoverEventCompat.of(HoverEvent.Action.SHOW_TEXT,
                                                ComponentUtil.tr("magiclib.ui.hover.switch_to", option)))))
                        .collect(Collectors.toList())),
                ComponentUtil.simple(" ]").withStyle(style -> style.withColor(ChatFormatting.YELLOW))));
        return 1;
    }

    public int setRule(CommandSourceStack source, @NotNull RuleOption ruleOption, String newValue) {
        if (!ruleOption.isEnabled()) {
            MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.disabled", ruleOption.getName()));
            return 1;
        }

        if (ruleOption.setValue(source, newValue) == null) {
            return 1;
        }

        MessageUtil.sendMessageCompat(source, ComponentUtil.compose(
                ComponentUtil.compose(
                        ComponentUtil.simple(this.getTranslatedRuleName(source, ruleOption.getName())),
                        ComponentUtil.simple(": "),
                        ComponentUtil.simple(newValue),
                        ComponentUtil.simple(", "),
                        ComponentUtil.compose(
                                ComponentUtil.simple("["),
                                ComponentUtil.tr("magiclib.ui.change_permanently"),
                                ComponentUtil.simple("]")).withStyle(style -> style
                                .withColor(ChatFormatting.AQUA)
                                .withClickEvent(ClickEventCompat.of(ClickEvent.Action.RUN_COMMAND,
                                        String.format("/%s setDefault %s %s",
                                                this.identifier, ruleOption.getName(), newValue)))
                                .withHoverEvent(HoverEventCompat.of(HoverEvent.Action.SHOW_TEXT,
                                        ComponentUtil.tr("magiclib.ui.hover.change_permanently",
                                                String.format("%s.conf", this.identifier))))))));
        return 1;
    }

    @SuppressWarnings("unchecked")
    public int setDefault(CommandSourceStack source, RuleOption ruleOption, String newValue) {
        if (this.locked()) {
            MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.locked"));
            return 1;
        }

        if (!ruleOption.isEnabled()) {
            MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.disabled", ruleOption.getName()));
            return 1;
        }

        if (ruleOption.setValue(source, newValue) == null) {
            return 1;
        }

        Class<?> carpetSM = WrappedSettingManager.class.getSuperclass();
        //#if MC > 11502
        Path path = ((SettingsManagerAccessor) this).invokeGetFile();
        Object conf = ReflectionUtil.invokeDeclared(carpetSM, "readSettingsFromConf", this, new Class<?>[]{Path.class}, path).orElseThrow(RuntimeException::new);
        //#else
        //$$ Pair<Map<String, String>, Boolean> conf = ((SettingsManagerAccessor) this).invokeReadSettingsFromConf();
        //#endif
        //#if MC > 11605
        //$$ Map<String, String> ruleMap = (Map<String, String>) ReflectionUtil.invoke(carpetSM.getName() + "$ConfigReadResult", "ruleMap", conf, null).orElseThrow(RuntimeException::new);
        //#else
        Map<String, String> ruleMap = ((Pair<Map<String, String>, Boolean>) conf).getLeft();
        //#endif
        ruleMap.put(ruleOption.getName(), newValue);
        //#if MC > 11802
        //$$ ReflectionUtil.invokeDeclared(carpetSM, "writeSettingsToConf", this, new Class[]{ReflectionUtil.getClass(carpetSM.getName() + "$ConfigReadResult").orElseThrow(RuntimeException::new)}, conf);
        //#else
        ((SettingsManagerAccessor) this).invokeWriteSettingsToConf(ruleMap);
        //#endif

        MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.set_default",
                this.getTranslatedRuleName(source, ruleOption.getName()), ruleOption.getStringValue()).withStyle(style ->
                style.withItalic(true).withColor(ChatFormatting.GRAY)));
        return 1;
    }

    @SuppressWarnings("unchecked")
    public int removeDefault(CommandSourceStack source, RuleOption ruleOption) {
        if (this.locked()) {
            MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.locked"));
            return 1;
        }

        if (!ruleOption.isEnabled()) {
            MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.disabled", ruleOption.getName()));
            return 1;
        }

        if (ruleOption.resetValue(source) == null) {
            return 1;
        }

        Class<?> carpetSM = WrappedSettingManager.class.getSuperclass();
        //#if MC > 11502
        Path path = ((SettingsManagerAccessor) this).invokeGetFile();
        Object conf = ReflectionUtil.invokeDeclared(carpetSM, "readSettingsFromConf", this, new Class<?>[]{Path.class}, path).orElseThrow(RuntimeException::new);
        //#else
        //$$ Pair<Map<String, String>, Boolean> conf = ((SettingsManagerAccessor) this).invokeReadSettingsFromConf();
        //#endif
        //#if MC > 11605
        //$$ Map<String, String> ruleMap = (Map<String, String>) ReflectionUtil.invoke(carpetSM.getName() + "$ConfigReadResult", "ruleMap", conf, null).orElseThrow(RuntimeException::new);
        //#else
        Map<String, String> ruleMap = ((Pair<Map<String, String>, Boolean>) conf).getLeft();
        //#endif
        ruleMap.remove(ruleOption.getName());
        //#if MC > 11802
        //$$ ReflectionUtil.invokeDeclared(carpetSM, "writeSettingsToConf", this, new Class[]{ReflectionUtil.getClass(carpetSM.getName() + "$ConfigReadResult").orElseThrow(RuntimeException::new)}, conf);
        //#else
        ((SettingsManagerAccessor) this).invokeWriteSettingsToConf(ruleMap);
        //#endif

        MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.reset_default",
                this.getTranslatedRuleName(source, ruleOption.getName()),
                ruleOption.getStringValue()).withStyle(style -> style
                .withItalic(true)
                .withColor(ChatFormatting.GRAY)));
        return 1;
    }

    @Deprecated
    public Collection<List<Component>> getMatchedSettings(@NotNull Collection<RuleOption> ruleOptions) {
        ruleOptions = ruleOptions.stream().filter((RuleOption::isEnabled)).collect(Collectors.toList());
        Collection<List<ComponentCompat>> collection = Lists.newArrayList();

        for (RuleOption ruleOption : ruleOptions) {
            List<ComponentCompat> components = Lists.newArrayList();
            components.add(ComponentCompat.literal(String.format("- %s", this.getTranslatedRuleName(ruleOption.getName()))).withStyle(style -> style
                    .withClickEvent(ClickEventCompat.of(ClickEvent.Action.RUN_COMMAND, String.format("/%s %s", this.identifier, ruleOption.getName())))
                    .withHoverEvent(HoverEventCompat.of(HoverEvent.Action.SHOW_TEXT, ComponentCompat.literal(this.trRuleDesc(ruleOption.getName())).withStyle(style1 -> style1.withColor(ChatFormatting.YELLOW))))));
            components.add(ComponentCompat.literal(" "));
            List<String> options = new ArrayList<>(ruleOption.getOptions());

            if (!options.contains(ruleOption.getStringValue())) {
                options.add(ruleOption.getStringValue());
            }

            for (String option : options) {
                components.add(ComponentCompat.literal(String.format("[%s]", option)).withStyle(style ->
                        style.withUnderlined(ruleOption.getStringValue().equals(option))
                                .withColor(ruleOption.isDefault() ? ChatFormatting.GRAY : ruleOption.getDefaultStringValue().equals(option) ? ChatFormatting.DARK_GREEN : ChatFormatting.YELLOW)
                                .withClickEvent(ruleOption.getStringValue().equals(option) || this.locked() ? null : ClickEventCompat.of(ClickEvent.Action.SUGGEST_COMMAND, String.format("/%s %s %s", this.identifier, ruleOption.getName(), option)))
                                .withHoverEvent(ruleOption.getStringValue().equals(option) || this.locked() ? null : HoverEventCompat.of(HoverEvent.Action.SHOW_TEXT, ComponentCompat.literal(this.trUI("hover.switch_to", option))))));
                components.add(ComponentCompat.literal(" "));
            }

            components.remove(components.size() - 1);
            collection.add(components);
        }

        return collection.stream().map(list -> list.stream().map(Provider::get).collect(Collectors.toList())).collect(Collectors.toList());
    }

    public Collection<ComponentCompat> getMatchedSettings(CommandSourceStack source,
                                                          @NotNull Collection<RuleOption> ruleOptions) {
        Collection<ComponentCompat> ret = Lists.newArrayList();
        ruleOptions.stream()
                .filter((RuleOption::isEnabled))
                .forEach(option -> {
                    List<ComponentCompat> components = Lists.newArrayList();
                    components.add(ComponentUtil.compose("- ",
                            this.getTranslatedRuleName(source, option.getName())).withStyle(style -> {
                        style.withClickEvent(ClickEventCompat.of(ClickEvent.Action.RUN_COMMAND,
                                String.format("/%s %s", this.identifier, option.getName())));
                        String descTranslationKey = String.format("%s.rule.%s.desc", this.identifier, option.getName());

                        if (I18n.exists(descTranslationKey)) {
                            style.withHoverEvent(HoverEventCompat.of(HoverEvent.Action.SHOW_TEXT,
                                    ComponentUtil.tr(descTranslationKey)
                                            .withStyle(style1 -> style1.withColor(ChatFormatting.YELLOW))));
                        }

                        return style;
                    }));
                    components.add(ComponentUtil.simple(" "));
                    List<String> options = Lists.newArrayList(option.getOptions());

                    if (!options.contains(option.getStringValue())) {
                        options.add(option.getStringValue());
                    }

                    components.add(ComponentUtil.join(ComponentUtil.simple(" "), options.stream()
                            .map(o -> ComponentUtil.compose("[", o, "]").withStyle(style -> style
                                    .withUnderlined(o.equals(option.getStringValue()))
                                    .withColor(option.isDefault() ? ChatFormatting.GRAY :
                                            o.equals(option.getDefaultStringValue()) ? ChatFormatting.DARK_GREEN :
                                                    ChatFormatting.YELLOW)
                                    .withClickEvent(o.equals(option.getStringValue()) || this.locked() ? null :
                                            ClickEventCompat.of(ClickEvent.Action.SUGGEST_COMMAND,
                                                    String.format("/%s %s %s", this.identifier, option.getName(), o)))
                                    .withHoverEvent(o.equals(option.getStringValue()) || this.locked() ? null :
                                            HoverEventCompat.of(HoverEvent.Action.SHOW_TEXT,
                                                    ComponentUtil.tr("magiclib.ui.hover.switch_to", o)))))
                            .collect(Collectors.toList())));
                    ret.add(ComponentUtil.join(ComponentUtil.empty(), components));
                });

        return ret;
    }

    private int listTagSettings(CommandSourceStack source, String category) {
        MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.matched_rule",
                this.getTranslatedFancyName(), category).withStyle(style -> style.withBold(true)));
        this.getMatchedSettings(source, this.OPTIONS
                        .values()
                        .stream()
                        .filter(option -> Arrays.stream(option.getCategory())
                                .collect(Collectors.toList()).contains(category))
                        .collect(Collectors.toList()))
                .forEach(c -> MessageUtil.sendMessageCompat(source, c));
        return 1;
    }

    private int displayMainMenu(CommandSourceStack source) {
        MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.current_rule",
                this.getTranslatedFancyName()).withStyle(style -> style.withBold(true)));
        this.getMatchedSettings(source, this.getNonDefaultRuleOption())
                .forEach(c -> MessageUtil.sendMessageCompat(source, c));
        WrappedSettingManager.printAllExtensionVersion(source);
        MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.browse", this.trFancyName())
                .withStyle(style -> style.withBold(true)));
        List<ComponentCompat> categories = Lists.newArrayList();
        categories.add(ComponentUtil.tr("magiclib.ui.tags"));
        this.CATEGORIES.stream()
                .sorted()
                .forEach(category -> {
                    if (this.getRuleOptionByCategory(category).stream().anyMatch(RuleOption::isEnabled)) {
                        categories.add(ComponentUtil.compose(
                                "[",
                                ComponentUtil.tr(String.format("%s.category.%s", this.identifier, category)),
                                "]").withStyle(style -> style
                                .withColor(ChatFormatting.AQUA)
                                .withClickEvent(ClickEventCompat.of(ClickEvent.Action.RUN_COMMAND,
                                        String.format("/%s list %s", this.identifier, category)))
                                .withHoverEvent(HoverEventCompat.of(HoverEvent.Action.SHOW_TEXT,
                                        ComponentUtil.tr("magiclib.ui.hover.list_all_category", category)))));
                    }
                });

        if (categories.size() < 2) {
            categories.add(ComponentUtil.tr("magiclib.ui.null")
                    .withStyle(style -> style.withColor(ChatFormatting.GRAY)));
        }

        MessageUtil.sendMessageCompat(source, ComponentUtil.join(ComponentUtil.simple(" "), categories));
        return 1;
    }

    @SuppressWarnings("unchecked")
    private int listDefaultSettings(CommandSourceStack source) {
        MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.default_rule",
                        this.getTranslatedFancyName(), String.format("%s.conf", this.identifier))
                .withStyle(style -> style.withBold(true)));
        Class<?> carpetSM = WrappedSettingManager.class.getSuperclass();
        //#if MC > 11502
        Path path = ((SettingsManagerAccessor) this).invokeGetFile();
        Object conf = ReflectionUtil.invokeDeclared(carpetSM, "readSettingsFromConf", this, new Class<?>[]{Path.class}, path).orElseThrow(RuntimeException::new);
        //#else
        //$$ Pair<Map<String, String>, Boolean> conf = ((SettingsManagerAccessor) this).invokeReadSettingsFromConf();
        //#endif
        //#if MC > 11605
        //$$ Set<String> defaults = ((Map<String, String>) ReflectionUtil.invoke(carpetSM.getName() + "$ConfigReadResult", "ruleMap", conf, null).orElseThrow(RuntimeException::new)).keySet();
        //#else
        Set<String> defaults = ((Pair<Map<String, String>, Boolean>) conf).getLeft().keySet();
        //#endif
        this.getMatchedSettings(source, this.OPTIONS
                        .values()
                        .stream()
                        .filter(ruleOption -> defaults.contains(ruleOption.getName()))
                        .collect(Collectors.toList()))
                .forEach(components -> MessageUtil.sendMessageCompat(source, components));
        return 1;
    }

    public int listAllSettings(CommandSourceStack source) {
        MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.all_rule",
                        this.getTranslatedFancyName())
                .withStyle(style -> style.withBold(true)));
        this.getMatchedSettings(source, this.OPTIONS.values())
                .forEach(c -> MessageUtil.sendMessageCompat(source, c));
        return 1;
    }

    public int searchRule(CommandSourceStack source, String content) {
        MessageUtil.sendMessageCompat(source, ComponentUtil.tr("magiclib.ui.matched_rule",
                        this.getTranslatedFancyName(), content)
                .withStyle(style -> style.withBold(true)));
        this.getMatchedSettings(source, this.OPTIONS
                        .values()
                        .stream()
                        .filter(ruleOption -> this.getTranslatedRuleName(source, ruleOption.getName())
                                .toLowerCase().contains(content.toLowerCase()))
                        .collect(Collectors.toList()))
                .forEach(c -> MessageUtil.sendMessageCompat(source, c));
        return 1;
    }

    @FunctionalInterface
    public interface RuleCallback {
        void callback(CommandSourceStack source, RuleOption rule, String value);
    }
}
