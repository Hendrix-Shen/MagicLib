package top.hendrixshen.magiclib.impl.malilib.config.option;

import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
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

    /**
     * Use this instead of {@code getKeybind().setCallback} directly
     * So the config statistic can be updated correctly
     */
    public void setCallBack(@Nullable IHotkeyCallback callback) {
        if (callback == null || !this.getMagicContainer().shouldStatisticHotkey()) {
            this.getKeybind().setCallback(callback);
        } else {
            this.getKeybind().setCallback(((action, key) -> {
                boolean ret = callback.onKeyAction(action, key);
                this.updateStatisticOnUse();
                return ret;
            }));
        }
    }

    public boolean isKeybindHeld() {
        return this.getKeybind().isKeybindHeld();
    }

    @Override
    public void onValueChanged() {
        this.onValueChanged(true);
    }

    @Override
    public void onValueChanged(boolean fromFile) {
        super.onValueChanged();

        if (!fromFile && this.getMagicContainer().shouldStatisticValueChange()) {
            this.updateStatisticOnUse();
        }
    }
}
