package top.hendrixshen.magiclib.config;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import fi.dy.masa.malilib.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class TranslatableConfigStringList extends ConfigStringList implements TranslatableConfig {
    private final String guiDisplayName;

    @Nullable
    private Consumer<ConfigBase<?>> valueChangedFromJsonCallback;

    public TranslatableConfigStringList(String prefix, String name, ImmutableList<String> defaultValue) {
        super(name, defaultValue, String.format("%s.%s.comment", prefix, name));
        this.guiDisplayName = String.format("%s.%s.name", prefix, name);
    }

    @Override
    public String getConfigGuiDisplayName() {
        return StringUtils.translate(this.guiDisplayName);
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
