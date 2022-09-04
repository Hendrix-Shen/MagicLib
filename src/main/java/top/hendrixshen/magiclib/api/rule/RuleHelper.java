package top.hendrixshen.magiclib.api.rule;

import carpet.settings.ParsedRule;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.util.ReflectUtil;

public class RuleHelper {
    public static RuleOption getRuleOption(@NotNull WrapperSettingManager settingManager, ParsedRule<?> parsedRule) {
        return settingManager.getRuleOption(parsedRule);
    }

    public static WrapperSettingManager getSettingManager(ParsedRule<?> parsedRule) {
        //#if MC > 11802
        return (WrapperSettingManager) ReflectUtil.getFieldValue("carpet.settings.ParsedRule", "realSettingsManager", parsedRule);
        //#else
        //$$ return (WrapperSettingManager) ReflectUtil.getFieldValue("carpet.settings.ParsedRule", "settingsManager", parsedRule);
        //#endif
    }
}
