package top.hendrixshen.magiclib.impl.carpet;

import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.carpet.impl.WrappedSettingManager;
import top.hendrixshen.magiclib.util.StringUtil;

public class MagicLibSettingManager extends WrappedSettingManager {
    public MagicLibSettingManager(String version, String identifier, String fancyName) {
        super(version, identifier, fancyName);
    }

    @Override
    public String getVersion() {
        return String.format("%s (%s)", super.getVersion(), this.tr(this.getCurrentLanguageCode(),
                String.format("%s.%s", MagicLibReference.getModIdentifier(), StringUtil.getVersionTypeCode(MagicLibReference.getModVersion()))));
    }
}
