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
public class TranslatableConfigString extends MagicConfigString {
    public TranslatableConfigString(String prefix, String name, String defaultValue) {
        super(prefix, name, defaultValue);
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
