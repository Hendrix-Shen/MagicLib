package top.hendrixshen.magiclib.malilib.impl.config;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.config.options.ConfigColor;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.malilib.api.config.IMagicConfigBase;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public class MagicConfigColor extends ConfigColor implements IMagicConfigBase {
    private final String prefix;

    @Nullable
    private Consumer<ConfigBase<?>> valueChangedFromJsonCallback;

    public MagicConfigColor(String prefix, String name, String defaultValue) {
        super(name, defaultValue, String.format("%s.%s.comment", prefix, name));
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
