package top.hendrixshen.magiclib.malilib.impl.config;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.malilib.api.config.IMagicConfigBase;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class MagicConfigHotkey extends ConfigHotkey implements IMagicConfigBase {
    private final String prefix;

    @Nullable
    private Consumer<ConfigBase<?>> valueChangedFromJsonCallback;

    public MagicConfigHotkey(String prefix, String name, String defaultStorageString) {
        super(name, defaultStorageString,
                String.format("%s.%s.comment", prefix, name),
                String.format("%s.%s.pretty_name", prefix, name));
        this.prefix = prefix;
    }

    public MagicConfigHotkey(String prefix, String name, String defaultStorageString, KeybindSettings settings) {
        super(name, defaultStorageString, settings,
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
