package top.hendrixshen.magiclib.impl.malilib.config.option;

import fi.dy.masa.malilib.config.options.ConfigString;
import lombok.Getter;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;

@Getter
public class MagicConfigString extends ConfigString implements MagicIConfigBase {
    private final String translationPrefix;

    public MagicConfigString(String translationPrefix, String name, String defaultValue) {
        super(name, defaultValue, String.format("%s.config.option.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }
}
