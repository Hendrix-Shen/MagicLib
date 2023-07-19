package top.hendrixshen.magiclib.malilib.impl.config;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.malilib.api.config.IMagicConfigBase;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class MagicConfigBooleanHotkeyed extends ConfigBooleanHotkeyed implements IMagicConfigBase {
    private final String prefix;

    @Nullable
    private Consumer<ConfigBase<?>> valueChangedFromJsonCallback;

    public MagicConfigBooleanHotkeyed(String prefix, String name, boolean defaultValue, String defaultHotkey) {
        super(name, defaultValue, defaultHotkey,
                String.format("%s.%s.comment", prefix, name),
                String.format("%s.%s.pretty_name", prefix, name));
        this.prefix = prefix;
    }

    public MagicConfigBooleanHotkeyed(String prefix, String name, boolean defaultValue, String defaultHotkey, KeybindSettings settings) {
        super(name, defaultValue, defaultHotkey, settings,
                String.format("%s.%s.comment", prefix, name),
                String.format("%s.%s.pretty_name", prefix, name));
        this.prefix = prefix;
    }

    @Override
    public void setValueFromJsonElement(JsonElement jsonElement) {
        super.setValueFromJsonElement(jsonElement);

        if (this.valueChangedFromJsonCallback != null) {
            this.valueChangedFromJsonCallback.accept(this);
        }
    }

    @Override
    @Nullable
    public Consumer<ConfigBase<?>> getValueChangedFromJsonCallback() {
        return this.valueChangedFromJsonCallback;
    }

    @Override
    public void setValueChangedFromJsonCallback(@Nullable Consumer<ConfigBase<?>> valueChangedFromJsonCallback) {
        this.valueChangedFromJsonCallback = valueChangedFromJsonCallback;
    }

    @Override
    public String getPrefix() {
        return this.prefix;
    }
}
