package top.hendrixshen.magiclib;

import top.hendrixshen.magiclib.config.ConfigManager;
import top.hendrixshen.magiclib.gui.ConfigGui;
import top.hendrixshen.magiclib.language.I18n;

public class MagicLibConfigGui extends ConfigGui {
    private static MagicLibConfigGui INSTANCE;

    private MagicLibConfigGui(String identifier, String defaultTab, ConfigManager configManager) {
        super(identifier, defaultTab, configManager,
                () -> I18n.get("magiclib.gui.title", MagicLibReference.getModVersion(),
                        I18n.get(String.format("magiclib.misc.versionType.%s", MagicLibReference.getModVersionType()))));
    }

    public static MagicLibConfigGui getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MagicLibConfigGui(MagicLibReference.getModId(), MagicLibConfigs.ConfigCategory.GENERIC, ConfigManager.get(MagicLibReference.getModId()));
        }
        return INSTANCE;
    }
}