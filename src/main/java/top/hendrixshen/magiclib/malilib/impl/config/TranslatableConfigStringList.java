package top.hendrixshen.magiclib.malilib.impl.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.language.api.I18n;
import top.hendrixshen.magiclib.malilib.api.config.TranslatableConfig;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class TranslatableConfigStringList extends ConfigStringList implements TranslatableConfig {
    private final String magicPrefix;

    @Nullable
    private Consumer<ConfigBase<?>> valueChangedFromJsonCallback;

    public TranslatableConfigStringList(String prefix, String name, ImmutableList<String> defaultValue) {
        super(name, defaultValue, String.format("%s.%s.comment", prefix, name));
        this.magicPrefix = prefix;
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
