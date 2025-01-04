package top.hendrixshen.magiclib.impl.carpet;

import top.hendrixshen.magiclib.SharedConstants;
import top.hendrixshen.magiclib.carpet.impl.WrappedSettingManager;

public class MagicLibSettingManager extends WrappedSettingManager {
    public MagicLibSettingManager() {
        super(SharedConstants.getMagiclibVersion(), SharedConstants.getMagiclibIdentifier(), SharedConstants.getMagiclibName());
    }
}
