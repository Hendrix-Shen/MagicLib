package top.hendrixshen.magiclib.config;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed;
import fi.dy.masa.malilib.hotkeys.KeybindSettings;
import fi.dy.masa.malilib.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.language.I18n;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class TranslatableConfigBooleanHotkeyed extends ConfigBooleanHotkeyed implements TranslatableConfig {
    private final String magicPrefix;

    @Nullable
    private Consumer<ConfigBase<?>> valueChangedFromJsonCallback;

    public TranslatableConfigBooleanHotkeyed(String prefix, String name, boolean defaultValue, String defaultHotkey) {
        super(name, defaultValue, defaultHotkey, String.format("%s.%s.comment", prefix, name),
                String.format("%s.%s.pretty_name", prefix, name));
        this.magicPrefix = prefix;
    }

    public TranslatableConfigBooleanHotkeyed(String prefix, String name, boolean defaultValue, String defaultHotkey, KeybindSettings settings) {
        super(name, defaultValue, defaultHotkey, settings, String.format("%s.%s.comment", prefix, name),
                String.format("%s.%s.pretty_name", prefix, name));
        this.magicPrefix = prefix;
    }

    @Override
    public String getPrettyName() {
        String ret = super.getPrettyName();
        if (ret.contains("pretty_name")) {
            ret = StringUtils.splitCamelCase(this.getConfigGuiDisplayName());
        }
        return ret;
    }

    @Override
    public String getComment() {
        return I18n.get(String.format("%s.%s.comment", magicPrefix, getName()));
    }

    @Override
    public String getConfigGuiDisplayName() {
        return I18n.get(String.format("%s.%s.name", magicPrefix, getName()));
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

}