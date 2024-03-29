package top.hendrixshen.magiclib.impl.malilib.config.option;

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
}
