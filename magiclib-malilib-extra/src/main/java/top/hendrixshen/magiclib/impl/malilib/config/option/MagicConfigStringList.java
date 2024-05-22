package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import lombok.Getter;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;

import java.util.List;

@Getter
public class MagicConfigStringList extends ConfigStringList implements MagicIConfigBase {
    private final String translationPrefix;

    public MagicConfigStringList(String prefix, String name, ImmutableList<String> defaultValue) {
        super(name, defaultValue, String.format("%s.config.option.%s.comment", prefix, name));
        this.translationPrefix = prefix;
    }

    @Override
    public String getPrettyName() {
        return MagicIConfigBase.super.getPrettyName();
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        List<String> oldValue = Lists.newArrayList(this.getStrings());
        super.setValueFromJsonElement(element);

        if (!oldValue.equals(this.getStrings())) {
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
