package top.hendrixshen.magiclib;

import top.hendrixshen.magiclib.config.ConfigManager;
import top.hendrixshen.magiclib.gui.ConfigGui;

public class MagiclibConfigGui extends ConfigGui {
    private static MagiclibConfigGui INSTANCE;

    private MagiclibConfigGui(String identifier, String defaultTab, ConfigManager configManager) {
        super(identifier, defaultTab, configManager, "Magiclib Test");
    }


    public static MagiclibConfigGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MagiclibConfigGui(MagicLibReference.getModId(), MagicLibConfigs.ConfigCategory.GENERIC, MagicLib.cm);
        }
        return INSTANCE;
    }
}