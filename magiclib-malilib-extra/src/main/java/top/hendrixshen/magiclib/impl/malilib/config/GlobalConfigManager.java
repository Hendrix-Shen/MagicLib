package top.hendrixshen.magiclib.impl.malilib.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fi.dy.masa.malilib.event.InputEventHandler;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigManager;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public final class GlobalConfigManager {
    @Getter(lazy = true)
    private static final GlobalConfigManager instance = new GlobalConfigManager();

    private final Map<String, MagicConfigManager> managers = Maps.newHashMap();
    private final List<ConfigContainer> CONTAINERS = Lists.newArrayList();
    private final Map<MagicIConfigBase, ConfigContainer> CONFIG_TO_CONTAINER = Maps.newLinkedHashMap();

    private GlobalConfigManager() {
    }

    void registerConfigContainer(ConfigContainer configContainer) {
        this.CONTAINERS.add(configContainer);
        this.CONFIG_TO_CONTAINER.put(configContainer.getConfig(), configContainer);
    }

    public @NotNull Collection<ConfigContainer> getAllContainers() {
        return Lists.newArrayList(this.CONTAINERS);
    }

    public @NotNull ValueContainer<ConfigContainer> getContainerByConfig(MagicIConfigBase config) {
        return ValueContainer.ofNullable(this.CONFIG_TO_CONTAINER.getOrDefault(config, null));
    }

    public boolean hasConfig(MagicIConfigBase config) {
        return this.getContainerByConfig(config).isPresent();
    }

    public static @NotNull MagicConfigManager getConfigManager(String identifier) {
        MagicConfigManager configManager = GlobalConfigManager.getInstance().managers.get(identifier);

        if (configManager == null) {
            configManager = new MagicConfigManagerImpl(identifier);
            GlobalConfigManager.getInstance().managers.put(identifier, configManager);
            InputEventHandler.getKeybindManager().registerKeybindProvider((MagicConfigManagerImpl) configManager);
        }

        return configManager;
    }
}
