package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.hotkeys.IHotkeyCallback;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.StringUtils;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;
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

    @Override
    public String getPrettyName() {
        return MagicIConfigBase.super.getPrettyName();
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

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        boolean oldValue = this.getBooleanValue();
        super.setValueFromJsonElement(element);

        if (oldValue != this.getBooleanValue()) {
            this.onValueChanged(true);
        }
    }

    @Override
    public void onValueChanged() {
        this.onValueChanged(false);
    }

    @Override
    public void onValueChanged(boolean fromFile) {
        super.onValueChanged();

        if (!fromFile && this.getMagicContainer().shouldStatisticValueChange()) {
            this.updateStatisticOnUse();
        }
    }
}
