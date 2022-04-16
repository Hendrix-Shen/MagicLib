package top.hendrixshen.magiclib;

import top.hendrixshen.magiclib.config.ConfigManager;
import top.hendrixshen.magiclib.gui.ConfigGui;

public class MagicLibConfigGui extends ConfigGui {
    private static MagicLibConfigGui INSTANCE;

    private MagicLibConfigGui(String identifier, String defaultTab, ConfigManager configManager) {
        super(identifier, defaultTab, configManager, "Magiclib Test");
    }

    public static MagicLibConfigGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MagicLibConfigGui(MagicLibReference.getModId(), MagicLibConfigs.ConfigCategory.GENERIC, ConfigManager.get(MagicLibReference.getModId()));
        }
        return INSTANCE;
    }
}