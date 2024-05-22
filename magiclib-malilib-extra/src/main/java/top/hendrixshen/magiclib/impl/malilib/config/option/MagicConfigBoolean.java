package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import lombok.Getter;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;

@Getter
public class MagicConfigBoolean extends ConfigBoolean implements MagicIConfigBase {
    private final String translationPrefix;

    public MagicConfigBoolean(String translationPrefix, String name, boolean defaultValue) {
        super(name, defaultValue, String.format("%s.config.option.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }

    @Override
    public String getPrettyName() {
        return MagicIConfigBase.super.getPrettyName();
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        boolean oldValue = this.getBooleanValue();
        super.setValueFromJsonElement(element);

        if (oldValue != this.getBooleanValue()) {
            this.onValueChanged(true);
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
