package top.hendrixshen.magiclib.impl.malilib.debug;

import top.hendrixshen.magiclib.impl.malilib.SharedConstants;
import top.hendrixshen.magiclib.impl.malilib.config.GlobalConfigManager;

public class MagicLibDebugHelper {
    public static void resetAllConfigStatistic() {
        GlobalConfigManager.getInstance().getAllContainers().forEach(configContainer ->
                configContainer.getStatistic().reset());
    }

    public static void resetMagicLibConfigStatistic() {
        SharedConstants.getConfigManager().getAllContainers().forEach(configContainer ->
                configContainer.getStatistic().reset());
    }
}
