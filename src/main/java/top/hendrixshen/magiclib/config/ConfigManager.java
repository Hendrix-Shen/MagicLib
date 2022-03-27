package top.hendrixshen.magiclib.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.*;
import fi.dy.masa.malilib.event.InputEventHandler;
import fi.dy.masa.malilib.hotkeys.IHotkey;
import fi.dy.masa.malilib.hotkeys.IKeybindManager;
import fi.dy.masa.malilib.hotkeys.IKeybindProvider;
import fi.dy.masa.malilib.util.Color4f;
import top.hendrixshen.magiclib.config.annotation.Config;
import top.hendrixshen.magiclib.config.annotation.Hotkey;
import top.hendrixshen.magiclib.config.annotation.Numeric;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class ConfigManager implements IKeybindProvider {
    private static final ConcurrentHashMap<String, ConfigManager> INSTANCES = new ConcurrentHashMap<>();
    private final String identifier;
    private final ConcurrentHashMap<String, Option> OPTIONS = new ConcurrentHashMap<>();
    private final LinkedBlockingQueue<String> CATEGORIES = new LinkedBlockingQueue<>();
    private final ConcurrentHashMap<ConfigBase<?>, Option> CONFIG_TO_OPTION = new ConcurrentHashMap<>();

    /**
     * Magic Configuration Manager constructor.
     *
     * @param identifier Your mod identifier.
     */
    protected ConfigManager(String identifier) {
        this.identifier = identifier;
    }

    /**
     * Get magic configuration manager under the specified name.
     *
     * @param identifier Your mod identifier.
     * @return Magic Configuration Manager for specified modid registered.
     */
    public static ConfigManager get(String identifier) {
        ConfigManager configManager = INSTANCES.get(identifier);
        if (configManager == null) {
            configManager = new ConfigManager(identifier);
            INSTANCES.put(identifier, configManager);
            InputEventHandler.getKeybindManager().registerKeybindProvider(configManager);
        }
        return configManager;
    }

    /**
     * Remove magic configuration manager under the specified name.
     *
     * @param identifier – Your mod identifier.
     */
    public static ConfigManager remove(String identifier) {
        ConfigManager cm = INSTANCES.remove(identifier);
        if (cm != null) {
            InputEventHandler.getKeybindManager().unregisterKeybindProvider(cm);
        }
        return cm;
    }

    private static void setFieldValue(Field field, Object obj, Object value) {
        try {
            field.set(obj, value);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static ImmutableList<String> immutableStringListHelper(List<?> list) {
        return ImmutableList.copyOf((List<String>) list);
    }

    @SuppressWarnings("unchecked")
    private static List<String> stringListHelper(List<?> list) {
        return (List<String>) list;
    }

    public boolean setValueChangeCallback(String optionName, Consumer<Option> callback) {
        return getOptionByName(optionName).map(option -> {
            option.setValueChangeCallback(callback);
            return true;
        }).isPresent();
    }

    /**
     * Get option under the specified name.
     *
     * @return A configuration.
     */
    public Optional<Option> getOptionByName(String optionName) {
        return Optional.ofNullable(OPTIONS.getOrDefault(optionName, null));
    }

    /**
     * Get config under the specified name with config type.
     */
    public <T> Optional<T> getConfig(Class<T> clazz, String optionName) {
        Optional<Option> optionOptional = getOptionByName(optionName);
        if (optionOptional.isPresent()) {
            return optionOptional.get().getConfig(clazz);
        } else {
            return Optional.empty();
        }
    }

    /**
     * Set values for the configuration under the specified name.
     *
     * @return True if set successfully.
     */
    public boolean setValue(String optionName, Object value) {
        Optional<Option> optionOptional = getOptionByName(optionName);
        if (optionOptional.isPresent()) {
            ConfigBase<?> configBase = optionOptional.get().getConfig();

            if (configBase instanceof ConfigBoolean && value instanceof Boolean) {
                ((ConfigBoolean) configBase).setBooleanValue((boolean) value);
            } else if (configBase instanceof ConfigInteger && value instanceof Integer) {
                ((ConfigInteger) configBase).setIntegerValue((int) value);
            } else if (configBase instanceof ConfigDouble && value instanceof Double) {
                ((ConfigDouble) configBase).setDoubleValue((double) value);
            } else if (configBase instanceof ConfigColor && value instanceof String) {
                ((ConfigColor) configBase).setValueFromString((String) value);
            } else if (configBase instanceof ConfigString && value instanceof String) {
                ((ConfigString) configBase).setValueFromString((String) value);
            } else if (configBase instanceof ConfigStringList && value instanceof List<?>) {
                ((ConfigStringList) configBase).setStrings(stringListHelper((List<?>) value));
            } else if (configBase instanceof ConfigOptionList && value instanceof String) {
                ((ConfigOptionList) configBase).setValueFromString((String) value);
            } else if (configBase instanceof ConfigOptionList && value instanceof IConfigOptionListEntry) {
                ((ConfigOptionList) configBase).setOptionListValue((IConfigOptionListEntry) value);
            } else {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Parsing configuration class to MagicLib Config Manager.
     *
     * @param configClass Your configuration class.
     */
    public void parseConfigClass(Class<?> configClass) {
        for (Field field : configClass.getDeclaredFields()) {
            Config annotation = field.getAnnotation(Config.class);
            if (annotation != null) {
                try {
                    Object configFieldObj = field.get(null);
                    ConfigBase<?> config;
                    Option option;
                    if (configFieldObj instanceof Boolean) {
                        Hotkey hotkey = field.getAnnotation(Hotkey.class);
                        if (hotkey == null) {
                            config = new TranslatableConfigBoolean(String.format("%s.config.%s", this.identifier, annotation.category()),
                                    field.getName(), (Boolean) configFieldObj);
                        } else {
                            config = new TranslatableConfigBooleanHotkeyed(String.format("%s.config.%s", this.identifier, annotation.category()),
                                    field.getName(), (Boolean) configFieldObj, hotkey.hotkey());
                        }
                        option = new Option(annotation, config);
                        config.setValueChangeCallback(c -> {
                            setFieldValue(field, null, ((ConfigBoolean) c).getBooleanValue());
                            option.getValueChangeCallback().accept(option);
                        });
                        ((TranslatableConfig) config).setValueChangedFromJsonCallback(
                                c -> setFieldValue(field, null, ((ConfigBoolean) c).getBooleanValue()));
                    } else if (configFieldObj instanceof Integer) {
                        Numeric numeric = field.getAnnotation(Numeric.class);
                        if (numeric == null) {
                            config = new TranslatableConfigInteger(String.format("%s.config.%s", this.identifier, annotation.category()),
                                    field.getName(), (Integer) configFieldObj);
                        } else {
                            config = new TranslatableConfigInteger(String.format("%s.config.%s", this.identifier, annotation.category()),
                                    field.getName(), (Integer) configFieldObj, (int) numeric.minValue(), (int) numeric.maxValue(), numeric.useSlider());
                        }
                        option = new Option(annotation, config);
                        config.setValueChangeCallback(c -> {
                            setFieldValue(field, null, ((ConfigInteger) c).getIntegerValue());
                            option.getValueChangeCallback().accept(option);
                        });
                        ((TranslatableConfig) config).setValueChangedFromJsonCallback(
                                c -> setFieldValue(field, null, ((ConfigInteger) c).getIntegerValue()));
                    } else if (configFieldObj instanceof String) {
                        config = new TranslatableConfigString(String.format("%s.config.%s", this.identifier, annotation.category()),
                                field.getName(), (String) configFieldObj);
                        option = new Option(annotation, config);
                        config.setValueChangeCallback(c -> {
                            setFieldValue(field, null, ((ConfigString) c).getStringValue());
                            option.getValueChangeCallback().accept(option);
                        });
                        ((TranslatableConfig) config).setValueChangedFromJsonCallback(
                                c -> setFieldValue(field, null, ((ConfigString) c).getStringValue()));
                    } else if (configFieldObj instanceof Color4f) {
                        config = new TranslatableConfigColor(String.format("%s.config.%s", this.identifier, annotation.category()),
                                field.getName(), String.format("#%08X", ((Color4f) configFieldObj).intValue));
                        option = new Option(annotation, config);
                        config.setValueChangeCallback(c -> {
                            setFieldValue(field, null, ((ConfigColor) c).getColor());
                            option.getValueChangeCallback().accept(option);
                        });
                        ((TranslatableConfig) config).setValueChangedFromJsonCallback(
                                c -> setFieldValue(field, null, ((ConfigColor) c).getColor()));
                    } else if (configFieldObj instanceof Double) {
                        Numeric numeric = field.getAnnotation(Numeric.class);
                        if (numeric == null) {
                            config = new TranslatableConfigDouble(String.format("%s.config.%s", this.identifier, annotation.category()),
                                    field.getName(), (Double) configFieldObj);
                        } else {
                            config = new TranslatableConfigDouble(String.format("%s.config.%s", this.identifier, annotation.category()),
                                    field.getName(), (Double) configFieldObj, numeric.minValue(), numeric.maxValue(), numeric.useSlider());
                        }
                        option = new Option(annotation, config);
                        config.setValueChangeCallback(c -> {
                            setFieldValue(field, null, ((ConfigDouble) c).getDoubleValue());
                            option.getValueChangeCallback().accept(option);
                        });
                        ((TranslatableConfig) config).setValueChangedFromJsonCallback(
                                c -> setFieldValue(field, null, ((ConfigDouble) c).getDoubleValue()));
                    } else if (field.getType() == ConfigHotkey.class) {
                        Hotkey hotkey = field.getAnnotation(Hotkey.class);
                        if (hotkey != null) {
                            config = new TranslatableConfigHotkey(String.format("%s.config.%s", this.identifier, annotation.category()),
                                    field.getName(), hotkey.hotkey());
                            option = new Option(annotation, config);
                            setFieldValue(field, null, config);
                        } else {
                            continue;
                        }
                    } else if (configFieldObj instanceof List<?>) {
                        config = new TranslatableConfigStringList(String.format("%s.config.%s", this.identifier, annotation.category()),
                                field.getName(), immutableStringListHelper((List<?>) configFieldObj));
                        option = new Option(annotation, config);
                        setFieldValue(field, null, ((ConfigStringList) config).getStrings());
                        config.setValueChangeCallback(c -> option.getValueChangeCallback().accept(option));
                    } else if (configFieldObj instanceof IConfigOptionListEntry) {
                        config = new TranslatableConfigOptionList(String.format("%s.config.%s", this.identifier, annotation.category()),
                                field.getName(), (IConfigOptionListEntry) configFieldObj);
                        option = new Option(annotation, config);
                        config.setValueChangeCallback(c -> {
                            setFieldValue(field, null, ((ConfigOptionList) c).getOptionListValue());
                            option.getValueChangeCallback().accept(option);
                        });
                        ((TranslatableConfig) config).setValueChangedFromJsonCallback(
                                c -> setFieldValue(field, null, ((ConfigOptionList) c).getOptionListValue()));
                    } else {
                        continue;
                    }
                    String category = annotation.category();
                    this.OPTIONS.put(option.getName(), option);
                    if (!this.CATEGORIES.contains(category)) {
                        this.CATEGORIES.add(category);
                    }
                    this.CONFIG_TO_OPTION.put(option.getConfig(), option);
                } catch (IllegalAccessException e) {
                    throw new IllegalStateException(e);
                }
            }
        }
    }

    /**
     * Get all configuration item categories.
     *
     * @return A list of categories.
     */
    public LinkedBlockingQueue<String> getCategories() {
        return this.CATEGORIES;
    }

    /**
     * Get all options items under the specified category.
     *
     * @return A list of options.
     */
    public Collection<Option> getOptionsByCategory(String category) {
        return this.OPTIONS.values().stream().filter(option -> option.getCategory().equals(category)).collect(Collectors.toList());
    }

    /**
     * Get all options items under the specified config.
     *
     * @return A configuration.
     */
    public Optional<Option> getOptionByConfig(ConfigBase<?> configBase) {
        return Optional.ofNullable(this.CONFIG_TO_OPTION.get(configBase));
    }

    @Override
    public void addKeysToMap(IKeybindManager iKeybindManager) {
        for (Option option : OPTIONS.values()) {
            ConfigBase<?> config = option.getConfig();
            if (config instanceof IHotkey) {
                iKeybindManager.addKeybindToMap(((IHotkey) config).getKeybind());

            }
        }
    }

    @Override
    public void addHotkeys(IKeybindManager iKeybindManager) {

    }
}
