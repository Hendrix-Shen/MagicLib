package top.hendrixshen.magiclib.malilib.impl.config;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBase;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.SharedConstants;
import top.hendrixshen.magiclib.helper.DeprecatedFeatureHelper;

import java.util.function.Consumer;

@Deprecated()
@ApiStatus.ScheduledForRemoval(inVersion = "0.8")
@Environment(EnvType.CLIENT)
public class TranslatableConfigInteger extends MagicConfigInteger {
    static {
        DeprecatedFeatureHelper.warn(SharedConstants.MAGICLIB_VERSION_0_8);
    }

    public TranslatableConfigInteger(String prefix, String name, int defaultValue) {
        super(prefix, name, defaultValue);
    }

    public TranslatableConfigInteger(String prefix, String name, int defaultValue, int minValue, int maxValue) {
        super(prefix, name, defaultValue, minValue, maxValue);
    }

    public TranslatableConfigInteger(String prefix, String name, int defaultValue, int minValue, int maxValue, boolean useSlider) {
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
