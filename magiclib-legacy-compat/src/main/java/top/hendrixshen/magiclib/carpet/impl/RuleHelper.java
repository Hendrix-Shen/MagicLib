package top.hendrixshen.magiclib.carpet.impl;

import carpet.settings.ParsedRule;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.util.ReflectionUtil;

//#if MC > 11900
//$$ @SuppressWarnings("removal")
//#endif
public class RuleHelper {
    public static RuleOption getRuleOption(@NotNull WrappedSettingManager settingManager, ParsedRule<?> parsedRule) {
        return settingManager.getRuleOption(parsedRule);
    }

    public static @NotNull WrappedSettingManager getSettingManager(ParsedRule<?> parsedRule) {
        return ReflectionUtil
                //#if MC > 11900
                //$$ .getDeclaredFieldValue("carpet.settings.ParsedRule", "realSettingsManager", parsedRule)
                //#else
                .getFieldValue("carpet.settings.ParsedRule", "settingsManager", parsedRule)
                //#endif
                .or(() -> {
                    throw new IllegalArgumentException("Rule %s doesn't have valid SettingManager");
                })
                .filter(s -> s instanceof WrappedSettingManager)
                .map(s -> (WrappedSettingManager) s)
                .orElseThrow(() -> {
                    throw new IllegalArgumentException(
                            String.format("Rule %s is not registered by WrapperSettingManager!",
                                    //#if MC > 11900
                                    //$$ parsedRule.name()
                                    //#else
                                    parsedRule.name
                                    //#endif
                            ));
                });
    }
}
