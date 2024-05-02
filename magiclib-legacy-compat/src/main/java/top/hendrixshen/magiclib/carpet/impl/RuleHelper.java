package top.hendrixshen.magiclib.carpet.impl;

import carpet.settings.ParsedRule;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.util.ReflectUtil;

import java.util.Optional;

//#if MC >= 11901
@SuppressWarnings("removal")
//#endif
public class RuleHelper {
    public static RuleOption getRuleOption(@NotNull WrappedSettingManager settingManager, ParsedRule<?> parsedRule) {
        return settingManager.getRuleOption(parsedRule);
    }

    public static @NotNull WrappedSettingManager getSettingManager(ParsedRule<?> parsedRule) {
        //#if MC > 11802
        Optional<?> settingManager = ReflectUtil.getDeclaredFieldValue("carpet.settings.ParsedRule", "realSettingsManager", parsedRule);
        //#else
        //$$ Optional<?> settingManager =  ReflectUtil.getFieldValue("carpet.settings.ParsedRule", "settingsManager", parsedRule);
        //#endif
        if (settingManager.isPresent()) {
            if (!(settingManager.get() instanceof WrappedSettingManager)) {
                //#if MC > 11802
                throw new IllegalArgumentException(String.format("Rule %s is not registered by WrapperSettingManager!", parsedRule.name()));
                //#else
                //$$ throw new IllegalArgumentException(String.format("Rule %s is not registered by WrapperSettingManager!", parsedRule.name));
                //#endif
            }
            return (WrappedSettingManager) settingManager.get();
        }
        throw new IllegalArgumentException("Rule %s doesn't have valid SettingManager");
    }
}
