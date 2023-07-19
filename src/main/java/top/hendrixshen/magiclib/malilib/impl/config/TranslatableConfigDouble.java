package top.hendrixshen.magiclib.malilib.impl.config;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

@Deprecated()
@ApiStatus.ScheduledForRemoval(inVersion = "0.8")
@Environment(EnvType.CLIENT)
public class TranslatableConfigDouble extends MagicConfigDouble {
    public TranslatableConfigDouble(String prefix, String name, double defaultValue) {
        super(prefix, name, defaultValue);
    }

    public TranslatableConfigDouble(String prefix, String name, double defaultValue, double minValue, double maxValue) {
        super(prefix, name, defaultValue, minValue, maxValue);
    }

    public TranslatableConfigDouble(String prefix, String name, double defaultValue, double minValue, double maxValue, boolean useSlider) {
        super(prefix, name, defaultValue, minValue, maxValue, useSlider);
    }

    @Override
    public void setValueFromJsonElement(JsonElement jsonElement) {
        super.setValueFromJsonElement(jsonElement);
    }

    @Override
    public @Nullable Consumer<ConfigBase<?>> getValueChangedFromJsonCallback() {
        return super.getValueChangedFromJsonCallback();
    }

    @Override
    public void setValueChangedFromJsonCallback(@Nullable Consumer<ConfigBase<?>> valueChangedFromJsonCallback) {
        super.setValueChangedFromJsonCallback(valueChangedFromJsonCallback);
    }

    @Override
    public String getName() {
        return super.getName();
    }

    @Override
    public String getPrettyName() {
        return super.getPrettyName();
    }

    @Override
    public String getComment() {
        return super.getComment();
    }
}
