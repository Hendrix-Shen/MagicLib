package top.hendrixshen.magiclib.api.malilib.config;

import fi.dy.masa.malilib.hotkeys.IHotkey;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.impl.malilib.config.ConfigContainer;
import top.hendrixshen.magiclib.impl.malilib.config.MagicConfigFactory;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;
import top.hendrixshen.magiclib.impl.malilib.config.option.MagicConfigHotkey;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.Collection;
import java.util.List;

public interface MagicConfigManager {
    static void setHotkeyCallback(@NotNull MagicConfigHotkey configHotkey, Runnable runnable,
                                  boolean cancelFurtherProcess) {
        configHotkey.setCallBack((keyAction, iKeybind) -> {
            runnable.run();
            return cancelFurtherProcess;
        });
    }

    String getIdentifier();

    MagicConfigFactory getConfigFactory();

    void parseConfigClass(@NotNull Class<?> configClass);

    Collection<String> getCategories();

    Collection<ConfigContainer> getContainers(String category);

    Collection<ConfigContainer> getAllContainers();

    ValueContainer<ConfigContainer> getContainerByConfig(MagicIConfigBase config);

    ValueContainer<ConfigContainer> getContainerByName(String name);

    List<IHotkey> getAllCustomHotkeys();

    MagicConfigGui.GuiSetting getGuiSetting();

    boolean hasConfig(String name);

    void onConfigLoaded();
}
