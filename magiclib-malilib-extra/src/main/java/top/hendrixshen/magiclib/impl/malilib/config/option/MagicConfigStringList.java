package top.hendrixshen.magiclib.impl.malilib.config.option;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import lombok.Getter;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;

@Getter
public class MagicConfigStringList extends ConfigStringList implements MagicIConfigBase {
    private final String translationPrefix;

    public MagicConfigStringList(String prefix, String name, ImmutableList<String> defaultValue) {
        super(name, defaultValue, String.format("%s.config.option.%s.comment", prefix, name));
        this.translationPrefix = prefix;
    }
}
