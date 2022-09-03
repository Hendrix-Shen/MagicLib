package top.hendrixshen.magiclib;

import top.hendrixshen.magiclib.api.rule.WrapperSettingManager;

public class MagicLibSettingManager extends WrapperSettingManager {
    public MagicLibSettingManager(String version, String identifier, String fancyName) {
        super(version, identifier, fancyName);
    }

    @Override
    public String getVersion() {
        return String.format("%s (%s)", super.getVersion(), this.tr(this.getCurrentLanguageCode(), String.format("magiclib.misc.versionType.%s", MagicLibReference.getModVersionType())));
    }
}
