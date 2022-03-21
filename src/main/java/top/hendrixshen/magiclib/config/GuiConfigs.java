package top.hendrixshen.magiclib.config;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.util.malilib.ConfigManager;

public class GuiConfigs extends top.hendrixshen.magiclib.impl.malilib.ConfigGui {
    private static GuiConfigs INSTANCE;

    private GuiConfigs(String identifier, String defaultTab, ConfigManager configManager) {
        super(identifier, defaultTab, configManager, "Magiclib Test");
    }


    public static GuiConfigs getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new GuiConfigs(MagicLibReference.getModId(), Configs.ConfigCategory.GENERIC, MagicLib.cm);
        }
        return INSTANCE;
    }
}