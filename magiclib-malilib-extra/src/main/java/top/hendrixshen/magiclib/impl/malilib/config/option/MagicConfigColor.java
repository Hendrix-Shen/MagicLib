package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigColor;
import fi.dy.masa.malilib.util.Color4f;
import lombok.Getter;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;

import java.util.Objects;

@Getter
public class MagicConfigColor extends ConfigColor implements MagicIConfigBase {
    private final String translationPrefix;

    public MagicConfigColor(String translationPrefix, String name, String defaultValue) {
        super(name, defaultValue, String.format("%s.config.option.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }

    @Override
    public String getPrettyName() {
        return MagicIConfigBase.super.getPrettyName();
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        Color4f oldValue = this.getColor();
        super.setValueFromJsonElement(element);

        if (Objects.equals(oldValue, this.getColor())) {
            this.onValueChanged(true);
        }
    }

    @Override
    public void onValueChanged() {
        this.onValueChanged(true);
    }

    @Override
    public void onValueChanged(boolean fromFile) {
        super.onValueChanged();

        if (!fromFile && this.getMagicContainer().shouldStatisticValueChange()) {
            this.updateStatisticOnUse();
        }
    }
}
