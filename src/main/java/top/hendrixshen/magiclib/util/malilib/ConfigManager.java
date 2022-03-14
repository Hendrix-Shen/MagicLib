package top.hendrixshen.magiclib.util.malilib;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.malilib.annotation.Config;
import top.hendrixshen.magiclib.impl.malilib.*;
import top.hendrixshen.magiclib.util.FabricUtil;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@SuppressWarnings("unused")
public class ConfigManager {
    private final String identifier;
    private final List<Option> OPTIONS = Lists.newArrayList();
    private final List<String> CATEGORIES = Lists.newArrayList();
    private final Map<String, List<Option>> CATEGORY_TO_OPTION = Maps.newLinkedHashMap();
    private final Map<IConfigBase, Option> CONFIG_TO_OPTION = Maps.newLinkedHashMap();
    private final Map<String, List<IConfigBase>> CATEGORY_TO_CONFIG = Maps.newLinkedHashMap();

    private boolean compatOldMalilib = !FabricUtil.isModLoaded("malilib", ">=0.11.4");
    private final List<String> COMPAT_CATEGORIES = Lists.newArrayList();

    private boolean hideDebug = true;
    private boolean hideDevOnly = true;
    private boolean hideDisabled = true;
    private boolean hideUnmatchedMinecraftVersion = true;

    /**
     * Magic Configuration Manager constructor.
     * @param identifier Your mod identifier.
     */
    public ConfigManager(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Parsing configuration class to MagicLib Config Manager.
     * @param configClass Your configuration class.
     */
    public void parseConfigClass(Class configClass) {
        for (Field field : configClass.getDeclaredFields()) {
            Config annotation = field.getAnnotation(Config.class);
            if (annotation != null) {
                try {
                    if (!(field.get(null) instanceof IConfigBase)) {
                        continue;
                    }
                    IConfigBase config = (IConfigBase) field.get(null);
                    Option option = new Option(annotation, config);
                    String category = annotation.category();
                    this.OPTIONS.add(option);
                    if (!this.CATEGORIES.contains(category)) {
                        this.CATEGORIES.add(category);
                    }
                    if (this.compatOldMalilib && config instanceof ConfigBooleanHotkeyed) {
                        String compatCategory = String.format("%s_hotkey", category);
                        if (!this.CATEGORIES.contains(compatCategory)) {
                            this.CATEGORIES.add(compatCategory);
                        }
                        if (!this.COMPAT_CATEGORIES.contains(compatCategory)) {
                            this.COMPAT_CATEGORIES.add(compatCategory);
                        }
                        this.CATEGORY_TO_CONFIG.computeIfAbsent(compatCategory, k -> Lists.newArrayList()).add(config);
                        this.CATEGORY_TO_OPTION.computeIfAbsent(compatCategory, k -> Lists.newArrayList()).add(option);
                    }
                    this.CATEGORY_TO_CONFIG.computeIfAbsent(option.getCategory(), k -> Lists.newArrayList()).add(config);
                    this.CATEGORY_TO_OPTION.computeIfAbsent(option.getCategory(), k -> Lists.newArrayList()).add(option);
                    this.CONFIG_TO_OPTION.put(option.getConfig(), option);
                } catch (IllegalAccessException e) {
                    MagicLib.getLogger().error(e);
                }
            }
        }
    }

    /**
     * Get configuration item display status. For more information, see {@link ConfigGui#getConfigs()}.
     * @return True if configuration items marked for debugging are displayed.
     */
    public boolean isHideDebug() {
        return hideDebug;
    }

    /**
     * Get configuration item display status. For more information, see {@link ConfigGui#getConfigs()}.
     * @return True if configuration items marked for development are displayed.
     */
    public boolean isHideDevOnly() {
        return hideDevOnly;
    }

    /**
     * Get configuration item display status. For more information, see {@link ConfigGui#getConfigs()}.
     * @return True if configuration items marked as incompatible with the current mods are displayed.
     */
    public boolean isHideDisabled() {
        return this.hideDisabled;
    }

    /**
     * Get configuration item display status. For more information, see {@link ConfigGui#getConfigs()}.
     * @return True if configuration items marked as incompatible with the current Minecraft version are displayed.
     */
    public boolean isHideUnmatchedMinecraftVersion() {
        return hideUnmatchedMinecraftVersion;
    }

    /**
     * Older versions of Malilib UI compatible. For more information, see {@link ConfigGui#getConfigs()}.
     * @return True if old Malilib tab compatibility is enabled.
     */
    public boolean isCompatOldMalilib() {
        return compatOldMalilib;
    }

    /**
     * Set configuration item display status. For more information, see {@link ConfigGui#getConfigs()}.
     */
    public void setHideDebug(boolean hideDebug) {
        this.hideDebug = hideDebug;
    }

    /**
     * Set configuration item display status. For more information, see {@link ConfigGui#getConfigs()}.
     */
    public void setHideDevOnly(boolean hideDevOnly) {
        this.hideDevOnly = hideDevOnly;
    }

    /**
     * Set configuration item display status. For more information, see {@link ConfigGui#getConfigs()}.
     */
    public void setHideDisabled(boolean hideDisabled) {
        this.hideDisabled = hideDisabled;
    }

    /**
     * Set configuration item display status. For more information, see {@link ConfigGui#getConfigs()}.
     */
    public void setHideUnmatchedMinecraftVersion(boolean hideUnmatchedMinecraftVersion) {
        this.hideUnmatchedMinecraftVersion = hideUnmatchedMinecraftVersion;
    }

    /**
     * Older versions of Malilib UI compatible. For more information, see {@link ConfigGui#getConfigs()}.
     */
    public void setCompatOldMalilib(boolean compatOldMalilib) {
        this.compatOldMalilib = compatOldMalilib;
    }

    /**
     * Get all configuration item categories.
     * @return A list of categories.
     */
    public List<String> getCategories() {
        return this.CATEGORIES;
    }

    /**
     * Get old Malilib compatible categories.
     * @return A list of categories.
     */
    public List<String> getCompatCategories() {
        return this.COMPAT_CATEGORIES;
    }

    /**
     * Get all configuration items under the specified category.
     * @return A list of configurations.
     */
    public List<IConfigBase> getConfigsByCategory(String category) {
        return this.CATEGORY_TO_CONFIG.getOrDefault(category, Collections.emptyList());
    }

    /**
     * Get all options items under the specified category.
     * @return A list of options.
     */
    public List<Option> getOptionsByCategory(String category) {
        return this.CATEGORY_TO_OPTION.getOrDefault(category, Collections.emptyList());
    }

    /**
     * Get all options stream.
     * @return Options Stream.
     */
    public Stream<IConfigBase> getAllConfigOptionStream() {
        return this.OPTIONS.stream().map(Option::getConfig);
    }

    /**
     * Get configuration according to option.
     * @return A configuration.
     */
    public Optional<Option> getOptionFromConfig(IConfigBase iConfigBase) {
        return Optional.ofNullable(this.CONFIG_TO_OPTION.get(iConfigBase));
    }

    /**
     * Get whether the specified configuration exists.
     * @return True if this configuration exists.
     */
	public boolean hasConfig(IConfigBase iConfigBase) {
		return this.getOptionFromConfig(iConfigBase).isPresent();
	}

    /**
     * Create a {@link TranslatableConfigBoolean} type configuration.
     * @param name Configuration name.
     * @param defaultValue Configuration default value.
     * @return A boolean configuration object.
     */
    public TranslatableConfigBoolean createBoolean(String name, boolean defaultValue) {
        return new TranslatableConfigBoolean(this.identifier, name, defaultValue);
    }

    /**
     * Create a {@link TranslatableConfigBooleanHotkeyed} type configuration.
     * @param name Configuration name.
     * @param defaultValue Configuration default value.
     * @param defaultHotkey Configuration default hotkey.
     * @return A boolean with hotkey configuration object.
     */
    public TranslatableConfigBooleanHotkeyed createBooleanHotkeyed(String name, boolean defaultValue, String defaultHotkey) {
        return new TranslatableConfigBooleanHotkeyed(this.identifier, name, defaultValue, defaultHotkey);
    }

    /**
     * Create a {@link TranslatableConfigBooleanHotkeyed} type configuration.
     * @param name Configuration name.
     * @param defaultValue Configuration default value.
     * @param defaultHotkey Configuration default hotkey.
     * @param settings Configuration default key bind setting.
     * @return A boolean with hotkey configuration object.
     */
    public TranslatableConfigBooleanHotkeyed createBooleanHotkeyed(String name, boolean defaultValue, String defaultHotkey, KeybindSettings settings) {
        return new TranslatableConfigBooleanHotkeyed(this.identifier, name, defaultValue, defaultHotkey, settings);
    }

    /**
     * Create a {@link TranslatableConfigColor} type configuration.
     * @param name Configuration name.
     * @param defaultValue Configuration default value.
     * @return A color configuration object.
     */
    public TranslatableConfigColor createColor(String name, String defaultValue) {
        return new TranslatableConfigColor(this.identifier, name, defaultValue);
    }

    /**
     * Create a {@link TranslatableConfigDouble} type configuration.
     * @param name Configuration name.
     * @param defaultValue Configuration default value.
     * @return A color configuration object.
     */
    public TranslatableConfigDouble createDouble(String name, double defaultValue) {
        return new TranslatableConfigDouble(this.identifier, name, defaultValue);
    }

    /**
     * Create a {@link TranslatableConfigDouble} type configuration.
     * @param name Configuration name.
     * @param defaultValue Configuration default value.
     * @param minValue Configuration min value limits.
     * @param maxValue Configuration max value limits.
     * @return A double configuration object.
     */
    public TranslatableConfigDouble createDouble(String name, double defaultValue, double minValue, double maxValue) {
        return new TranslatableConfigDouble(this.identifier, name, defaultValue, minValue, maxValue);
    }

    /**
     * Create a {@link TranslatableConfigDouble} type configuration.
     * @param name Configuration name.
     * @param defaultValue Configuration default value.
     * @param minValue Configuration min value limits.
     * @param maxValue Configuration max value limits.
     * @param useSlider Enable or disable the slider by default.
     * @return A double configuration object.
     */
    public TranslatableConfigDouble createDouble(String name, double defaultValue, double minValue, double maxValue, boolean useSlider) {
        return new TranslatableConfigDouble(this.identifier, name, defaultValue, minValue, maxValue, useSlider);
    }

    /**
     * Create a {@link TranslatableConfigHotkey} type configuration.
     * @param name Configuration name.
     * @param defaultStorageString Configuration default value.
     * @return A hotkey configuration object.
     */
    public TranslatableConfigHotkey createHotkey(String name, String defaultStorageString) {
        return new TranslatableConfigHotkey(this.identifier, name, defaultStorageString);
    }

    /**
     * Create a {@link TranslatableConfigHotkey} type configuration.
     * @param name Configuration name.
     * @param defaultStorageString Configuration default value.
     * @param settings Configuration default key bind setting.
     * @return A hotkey configuration object.
     */
    public TranslatableConfigHotkey createHotkey(String name, String defaultStorageString, KeybindSettings settings) {
        return new TranslatableConfigHotkey(this.identifier, name, defaultStorageString, settings);
    }

    /**
     * Create a {@link TranslatableConfigInteger} type configuration.
     * @param name Configuration name.
     * @param defaultValue Configuration default value.
     * @return An integer configuration object.
     */
    public TranslatableConfigInteger createInteger(String name, int defaultValue) {
        return new TranslatableConfigInteger(this.identifier, name, defaultValue);
    }

    /**
     * Create a {@link TranslatableConfigInteger} type configuration.
     * @param name Configuration name.
     * @param defaultValue Configuration default value.
     * @param minValue Configuration min value limits.
     * @param maxValue Configuration max value limits.
     * @return An integer configuration object.
     */
    public TranslatableConfigInteger createInteger(String name, int defaultValue, int minValue, int maxValue) {
        return new TranslatableConfigInteger(this.identifier, name, defaultValue, minValue, maxValue);
    }

    /**
     * Create a {@link TranslatableConfigInteger} type configuration.
     * @param name Configuration name.
     * @param defaultValue Configuration default value.
     * @param minValue Configuration min value limits.
     * @param maxValue Configuration max value limits.
     * @param useSlider Enable or disable the slider by default.
     * @return An integer configuration object.
     */
    public TranslatableConfigInteger createInteger(String name, int defaultValue, int minValue, int maxValue, boolean useSlider) {
        return new TranslatableConfigInteger(this.identifier, name, defaultValue, minValue, maxValue, useSlider);
    }

    /**
     * Create a {@link TranslatableConfigOptionList} type configuration.
     * @param name Configuration name.
     * @param defaultValue Configuration default value.
     * @return An option list configuration object.
     */
    public TranslatableConfigOptionList createOptionList(String name, IConfigOptionListEntry defaultValue) {
        return new TranslatableConfigOptionList(this.identifier, name, defaultValue);
    }

    /**
     * Create a {@link TranslatableConfigString} type configuration.
     * @param name Configuration name.
     * @param defaultValue Configuration default value.
     * @return A string configuration object.
     */
    public TranslatableConfigString createString(String name, String defaultValue) {
        return new TranslatableConfigString(this.identifier, name, defaultValue);
    }

    /**
     * Create a {@link TranslatableConfigStringList} type configuration.
     * @param name Configuration name.
     * @param defaultValue Configuration default value.
     * @return A string list configuration object.
     */
    public TranslatableConfigStringList createStringList(String name, ImmutableList<String> defaultValue) {
        return new TranslatableConfigStringList(this.identifier, name, defaultValue);
    }
}
