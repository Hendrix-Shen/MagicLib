package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.gson.JsonElement;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.ConfigOptionList;
import lombok.Getter;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;

@Getter
public class MagicConfigOptionList extends ConfigOptionList implements MagicIConfigBase {
    private final String translationPrefix;

    public MagicConfigOptionList(String translationPrefix, String name, IConfigOptionListEntry defaultValue) {
        super(name, defaultValue, String.format("%s.config.option.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }

    @Override
    public void setValueFromJsonElement(JsonElement element) {
        IConfigOptionListEntry oldValue = this.getOptionListValue();
        super.setValueFromJsonElement(element);

        // malilb uses != instead of equals() in ConfigOptionList.setOptionListValue, so we do the same here
        if (oldValue != this.getOptionListValue()) {
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
