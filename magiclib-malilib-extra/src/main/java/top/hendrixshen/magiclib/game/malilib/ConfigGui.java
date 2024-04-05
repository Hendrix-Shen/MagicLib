package top.hendrixshen.magiclib.game.malilib;

import fi.dy.masa.malilib.gui.GuiBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.api.i18n.minecraft.I18n;
import top.hendrixshen.magiclib.impl.malilib.SharedConstants;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

public class ConfigGui extends MagicConfigGui {
    @Nullable
    private static ConfigGui currentInstance = null;

    public ConfigGui() {
        super(SharedConstants.getModIdentifier(), SharedConstants.getConfigManager(),
                I18n.tr(String.format("%s.config.gui.title", SharedConstants.getModIdentifier()),
                        SharedConstants.getModVersion(), SharedConstants.getTranslatedModVersionType()));
    }

    @Override
    public void init() {
        super.init();
        ConfigGui.currentInstance = this;
    }

    @Override
    public void removed() {
        super.removed();
        ConfigGui.currentInstance = null;
    }

    @Override
    public boolean isDebug() {
        return Configs.debug.getBooleanValue();
    }

    public static void openGui() {
        GuiBase.openGui(new ConfigGui());
    }

    public static @NotNull ValueContainer<ConfigGui> getCurrentInstance() {
        return ValueContainer.ofNullable(ConfigGui.currentInstance);
    }
}
