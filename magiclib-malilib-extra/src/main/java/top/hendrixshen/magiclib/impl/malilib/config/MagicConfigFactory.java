package top.hendrixshen.magiclib.impl.malilib.config;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import top.hendrixshen.magiclib.impl.malilib.config.option.*;

public class MagicConfigFactory {
    private final String identifier;

    public MagicConfigFactory(String identifier) {
        this.identifier = identifier;
    }

    public MagicConfigBoolean newConfigBoolean(String name, boolean defaultValue) {
        return new MagicConfigBoolean(this.identifier, name, defaultValue);
    }

    public MagicConfigBooleanHotkeyed newConfigBooleanHotkeyed(String name, boolean defaultValue) {
        return new MagicConfigBooleanHotkeyed(this.identifier, name, defaultValue, "");
    }

    public MagicConfigBooleanHotkeyed newConfigBooleanHotkeyed(String name, boolean defaultValue,
                                                               String defaultHotkey, KeybindSettings settings) {
        return new MagicConfigBooleanHotkeyed(this.identifier, name, defaultValue, defaultHotkey, settings);
    }

    public MagicConfigColor newConfigColor(String name) {
        return new MagicConfigColor(this.identifier, name, "#C00F0F0F");
    }

    public MagicConfigColor newConfigColor(String name, String defaultValue) {
        return new MagicConfigColor(this.identifier, name, defaultValue);
    }

    public MagicConfigDouble newConfigDouble(String name, double defaultValue) {
        return new MagicConfigDouble(this.identifier, name, defaultValue);
    }

    public MagicConfigDouble newConfigDouble(String name, double defaultValue, double minValue, double maxValue) {
        return new MagicConfigDouble(this.identifier, name, defaultValue, minValue, maxValue);
    }

    public MagicConfigDouble newConfigDouble(String name, double defaultValue, double minValue, double maxValue,
                                             boolean useSlider) {
        return new MagicConfigDouble(this.identifier, name, defaultValue, minValue, maxValue, useSlider);
    }

    public MagicConfigHotkey newConfigHotkey(String name) {
        return new MagicConfigHotkey(this.identifier, name, "");
    }

    public MagicConfigHotkey newConfigHotkey(String name, String defaultHotkey) {
        return new MagicConfigHotkey(this.identifier, name, defaultHotkey);
    }

    public MagicConfigHotkey newConfigHotkey(String name, String defaultHotkey, KeybindSettings settings) {
        return new MagicConfigHotkey(this.identifier, name, defaultHotkey, settings);
    }

    public MagicConfigHotkeyWithSwitch newConfigHotkeyWithSwitch(String name, boolean defaultEnableState) {
        return new MagicConfigHotkeyWithSwitch(this.identifier, name, defaultEnableState, "");
    }

    public MagicConfigHotkeyWithSwitch newConfigHotkeyWithSwitch(String name, boolean defaultEnableState,
                                                                 String defaultHotkey) {
        return new MagicConfigHotkeyWithSwitch(this.identifier, name, defaultEnableState,
                defaultHotkey);
    }

    public MagicConfigHotkeyWithSwitch newConfigHotkeyWithSwitch(String name, boolean defaultEnableState,
                                                                 String defaultHotkey, KeybindSettings settings) {
        return new MagicConfigHotkeyWithSwitch(this.identifier, name, defaultEnableState,
                defaultHotkey, settings);
    }

    public MagicConfigInteger newConfigInteger(String name, int defaultValue) {
        return new MagicConfigInteger(this.identifier, name, defaultValue);
    }

    public MagicConfigInteger newConfigInteger(String name, int defaultValue, int minValue, int maxValue) {
        return new MagicConfigInteger(this.identifier, name, defaultValue, minValue, maxValue);
    }

    public MagicConfigInteger newConfigInteger(String name, int defaultValue, int minValue, int maxValue,
                                               boolean useSlider) {
        return new MagicConfigInteger(this.identifier, name, defaultValue, minValue, maxValue, useSlider);
    }

    public MagicConfigOptionList newConfigOptionList(String name, IConfigOptionListEntry defaultValue) {
        return new MagicConfigOptionList(this.identifier, name, defaultValue);
    }

    public MagicConfigOptionListHotkeyed newConfigOptionListHotkeyed(String name, IConfigOptionListEntry defaultValue) {
        return new MagicConfigOptionListHotkeyed(this.identifier, name, defaultValue, "");
    }

    public MagicConfigOptionListHotkeyed newConfigOptionListHotkeyed(String name, IConfigOptionListEntry defaultValue,
                                                                     String defaultHotkey) {
        return new MagicConfigOptionListHotkeyed(this.identifier, name, defaultValue, defaultHotkey);
    }

    public MagicConfigOptionListHotkeyed newConfigOptionListHotkeyed(String name, IConfigOptionListEntry defaultValue,
                                                                     String defaultHotkey, KeybindSettings settings) {
        return new MagicConfigOptionListHotkeyed(this.identifier, name, defaultValue, defaultHotkey, settings);
    }

    public MagicConfigString newConfigString(String name) {
        return new MagicConfigString(this.identifier, name, "");
    }

    public MagicConfigString newConfigString(String name, String defaultValue) {
        return new MagicConfigString(this.identifier, name, defaultValue);
    }

    public MagicConfigStringList newConfigStringList(String name) {
        return new MagicConfigStringList(this.identifier, name, ImmutableList.of());
    }

    public MagicConfigStringList newConfigStringList(String name, ImmutableList<String> defaultValue) {
        return new MagicConfigStringList(this.identifier, name, defaultValue);
    }
}
