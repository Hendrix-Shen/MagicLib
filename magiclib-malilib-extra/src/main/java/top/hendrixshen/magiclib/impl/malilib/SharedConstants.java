package top.hendrixshen.magiclib.impl.malilib;

import fi.dy.masa.malilib.gui.GuiBase;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import top.hendrixshen.magiclib.api.i18n.I18n;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigHandler;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigManager;
import top.hendrixshen.magiclib.impl.malilib.config.GlobalConfigManager;
import top.hendrixshen.magiclib.impl.malilib.config.MagicConfigHandlerImpl;
import top.hendrixshen.magiclib.util.VersionUtil;

public class SharedConstants {
    @Getter
    private static final String modIdentifier = "@MOD_IDENTIFIER@";
    @Getter
    private static final String modName = "@MOD_NAME@";
    @Getter
    private static final String modVersion = "@MOD_VERSION@";
    @Getter
    private static final String modVersionType = VersionUtil.getVersionType(SharedConstants.modVersion);
    @Getter
    private static final MagicConfigManager configManager = GlobalConfigManager
            .getConfigManager(top.hendrixshen.magiclib.SharedConstants.getMagiclibIdentifier());
    @Getter
    private static final MagicConfigHandler configHandler = new MagicConfigHandlerImpl(SharedConstants.configManager,
            2);

    public static @NotNull String getTranslatedModVersionType() {
        return VersionUtil.translateVersionType(SharedConstants.modVersion);
    }

    public static String getColoredEnableStateText(boolean enable) {
        return enable
                ? GuiBase.TXT_DARK_GREEN + I18n.tr("magiclib.config.gui.element.enable_state.enabled") + GuiBase.TXT_RST
                : GuiBase.TXT_DARK_RED + I18n.tr("magiclib.config.gui.element.enable_state.disabled") + GuiBase.TXT_RST;
    }
}
