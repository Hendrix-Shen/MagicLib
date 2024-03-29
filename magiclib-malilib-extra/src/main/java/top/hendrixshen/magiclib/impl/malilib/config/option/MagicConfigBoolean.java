package top.hendrixshen.magiclib.impl.malilib.config.option;

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
}
