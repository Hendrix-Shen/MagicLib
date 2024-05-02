package top.hendrixshen.magiclib.malilib.impl.config;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigInteger;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.malilib.api.config.IMagicConfigBase;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class MagicConfigInteger extends ConfigInteger implements IMagicConfigBase {
    private final String prefix;

    @Nullable
    private Consumer<ConfigBase<?>> valueChangedFromJsonCallback;

    public MagicConfigInteger(String prefix, String name, int defaultValue) {
        super(name, defaultValue, String.format("%s.%s.comment", prefix, name));
        this.prefix = prefix;
    }

    @Override
    public String getConfigGuiDisplayName() {
        return IMagicConfigBase.super.getConfigGuiDisplayName();
    }

    public MagicConfigInteger(String prefix, String name, int defaultValue, int minValue, int maxValue) {
        super(name, defaultValue, minValue, maxValue, String.format("%s.%s.comment", prefix, name));
        this.prefix = prefix;
    }

    public MagicConfigInteger(String prefix, String name, int defaultValue, int minValue, int maxValue, boolean useSlider) {
        super(name, defaultValue, minValue, maxValue, useSlider, String.format("%s.%s.comment", prefix, name));
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
