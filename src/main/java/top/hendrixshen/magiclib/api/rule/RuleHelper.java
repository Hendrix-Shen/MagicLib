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
        Object settingManager = ReflectUtil.getFieldValue("carpet.settings.ParsedRule", "realSettingsManager", parsedRule);
        //#else
        //$$ Object settingManager =  ReflectUtil.getFieldValue("carpet.settings.ParsedRule", "settingsManager", parsedRule);
        //#endif

        if (!(settingManager instanceof WrapperSettingManager)) {
            //#if MC > 11802
            throw new IllegalArgumentException(String.format("Rule %s is not registered by WrapperSettingManager!", parsedRule.name()));
            //#else
            //$$ throw new IllegalArgumentException(String.format("Rule %s is not registered by WrapperSettingManager!", parsedRule.name));
            //#endif
        }
        return (WrapperSettingManager) settingManager;
    }
}
