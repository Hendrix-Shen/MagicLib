package top.hendrixshen.magiclib.impl.malilib.config;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.malilib.annotation.Config;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigManager;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.lang.reflect.Field;
import java.util.*;
import java.util.stream.Collectors;

public class MagicConfigManagerImpl implements MagicConfigManager, IKeybindProvider {
    @Getter
    private final MagicConfigFactory configFactory;
    @Getter
    private final String identifier;
    @Getter
    private final MagicConfigGui.GuiSetting guiSetting = new MagicConfigGui.GuiSetting();
    private final List<ConfigContainer> CONTAINERS = Lists.newArrayList();
    private final Set<String> CATEGORIES = Sets.newHashSet(Config.defaultCategory);
    private final Map<String, List<ConfigContainer>> CATEGORY_TO_CONTAINERS = Maps.newLinkedHashMap();
    private final Map<MagicIConfigBase, ConfigContainer> CONFIG_TO_CONTAINER = Maps.newLinkedHashMap();
    private final Map<String, ConfigContainer> NAME_TO_CONTAINER = Maps.newLinkedHashMap();

    /**
     * Magic Configuration Manager constructor.
     *
     * @param identifier Your mod identifier.
     */
    protected MagicConfigManagerImpl(String identifier) {
        this.identifier = identifier;
        this.configFactory = new MagicConfigFactory(identifier);
    }

    @Override
    public final void parseConfigClass(@NotNull Class<?> configClass) {
        for (Field field : configClass.getDeclaredFields()) {
            Config annotation = field.getAnnotation(Config.class);

            if (annotation == null) {
                continue;
            }

            try {
                Object config = field.get(null);

                if (!(config instanceof MagicIConfigBase)) {
                    MagicLib.getLogger().warn("{} is not a subclass of MagicIConfigBase, skipping!", config);
                    continue;
                }

                ConfigContainer configContainer = new ConfigContainer(annotation, (MagicIConfigBase) config);
                this.CONTAINERS.add(configContainer);
                this.CATEGORIES.add(configContainer.getCategory());
                this.CATEGORY_TO_CONTAINERS.computeIfAbsent(configContainer.getCategory(),
                        category -> Lists.newArrayList()).add(configContainer);
                this.CONFIG_TO_CONTAINER.put(configContainer.getConfig(), configContainer);
                this.NAME_TO_CONTAINER.put(configContainer.getName(), configContainer);
                GlobalConfigManager.getInstance().registerConfigContainer(configContainer);
            } catch (IllegalAccessException e) {
                MagicLib.getLogger().error(e);
            }
        }
    }

    @Override
    public Collection<String> getCategories() {
        return Lists.newArrayList(this.CATEGORIES);
    }

    @Override
    public Collection<ConfigContainer> getContainers(@NotNull String category) {
        if (category.equals(Config.defaultCategory)) {
            return this.CONTAINERS;
        }

        return this.CATEGORY_TO_CONTAINERS.getOrDefault(category, Collections.emptyList());
    }

    @Override
    public Collection<ConfigContainer> getAllContainers() {
        return Lists.newArrayList(this.CONTAINERS);
    }

    @Override
    public ValueContainer<ConfigContainer> getContainerByConfig(MagicIConfigBase config) {
        return ValueContainer.ofNullable(this.CONFIG_TO_CONTAINER.getOrDefault(config, null));
    }

    @Override
    public ValueContainer<ConfigContainer> getContainerByName(String name) {
        return ValueContainer.ofNullable(this.NAME_TO_CONTAINER.getOrDefault(name, null));
    }

    @Override
    public List<IHotkey> getAllCustomHotkeys() {
        return this.CONTAINERS.stream()
                .map(ConfigContainer::getConfig)
                .filter(config -> config instanceof IHotkey)
                .map(config -> (IHotkey) config)
                .collect(Collectors.toList());
    }

    @Override
    public boolean hasConfig(String name) {
        return this.getContainerByName(name).isPresent();
    }

    @Override
    public void onConfigLoaded() {
    }

    @Override
    public void addKeysToMap(IKeybindManager iKeybindManager) {
        this.getAllCustomHotkeys().forEach(iHotkey -> iKeybindManager.addKeybindToMap(iHotkey.getKeybind()));
    }

    @Override
    public void addHotkeys(@NotNull IKeybindManager iKeybindManager) {
        iKeybindManager.addHotkeysForCategory(this.identifier,
                String.format("%s.hotkeys.category.main", this.identifier), this.getAllCustomHotkeys());
    }
}
