package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigString;
import lombok.Getter;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;

import java.util.Objects;

@Getter
public class MagicConfigString extends ConfigString implements MagicIConfigBase {
    private final String translationPrefix;

    public MagicConfigString(String translationPrefix, String name, String defaultValue) {
        super(name, defaultValue, String.format("%s.config.option.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }

    @Override
    public String getPrettyName() {
        return MagicIConfigBase.super.getPrettyName();
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        String oldValue = this.getStringValue();
        super.setValueFromJsonElement(element);

        if (!Objects.equals(oldValue, this.getStringValue())) {
            this.onValueChanged();
        }
    }

    @Override
    public void onValueChanged() {
        this.onValueChanged(false);
    }

    @Override
    public void onValueChanged(boolean fromFile) {
        super.onValueChanged();

        if (!fromFile && this.getMagicContainer().shouldStatisticValueChange()) {
            this.updateStatisticOnUse();
        }
    }
}
