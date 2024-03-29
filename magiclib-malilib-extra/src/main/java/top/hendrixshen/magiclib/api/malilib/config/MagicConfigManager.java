package top.hendrixshen.magiclib.api.malilib.config;

import fi.dy.masa.malilib.hotkeys.IHotkey;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.impl.malilib.config.ConfigContainer;
import top.hendrixshen.magiclib.impl.malilib.config.MagicConfigFactory;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;
import top.hendrixshen.magiclib.util.collect.ValueContainer;
import top.hendrixshen.magiclib.util.serializable.JsonSaveAble;

import java.util.Collection;
import java.util.List;

public interface MagicConfigManager {
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
