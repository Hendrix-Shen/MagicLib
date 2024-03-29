package top.hendrixshen.magiclib.impl.malilib.config.option;

import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.StringUtils;
import lombok.Getter;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;

@Getter
public class MagicConfigBooleanHotkeyed extends ConfigBooleanHotkeyed implements MagicIConfigBase {
    private final String translationPrefix;

    public MagicConfigBooleanHotkeyed(String translationPrefix, String name,
                                      boolean defaultValue, String defaultHotkey) {
        super(name, defaultValue, defaultHotkey,
                String.format("%s.config.option.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }

    public MagicConfigBooleanHotkeyed(String translationPrefix, String name,
                                      boolean defaultValue, String defaultHotkey, KeybindSettings settings) {
        super(name, defaultValue, defaultHotkey, settings,
                String.format("%s.config.option.%s.comment", translationPrefix, name),
                StringUtils.splitCamelCase(name));
        this.translationPrefix = translationPrefix;
    }
}
