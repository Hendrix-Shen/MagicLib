package top.hendrixshen.magiclib.impl.malilib.config.option;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import lombok.Getter;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;

@Getter
public class MagicConfigHotkey extends ConfigHotkey implements MagicIConfigBase {
    private final String translationPrefix;

    public MagicConfigHotkey(String translationPrefix, String name, String defaultStorageString) {
        super(name, defaultStorageString, String.format("%s.config.option.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }

    public MagicConfigHotkey(String translationPrefix, String name, String defaultStorageString,
                             KeybindSettings settings) {
        super(name, defaultStorageString, settings, String.format("%s.config.option.%s.comment",
                translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }

    public boolean isKeybindHeld() {
        return this.getKeybind().isKeybindHeld();
    }
}
