package top.hendrixshen.magiclib.impl.malilib.config.option;

import fi.dy.masa.malilib.config.options.ConfigDouble;
import lombok.Getter;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;

@Getter
public class MagicConfigDouble extends ConfigDouble implements MagicIConfigBase {
    private final String translationPrefix;

    public MagicConfigDouble(String translationPrefix, String name, double defaultValue) {
        super(name, defaultValue, String.format("%s.config.option.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }

    public MagicConfigDouble(String translationPrefix, String name, double defaultValue,
                             double minValue, double maxValue) {
        super(name, defaultValue, minValue, maxValue, String.format("%s.config.option.%s.comment",
                translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }

    public MagicConfigDouble(String translationPrefix, String name, double defaultValue,
                             double minValue, double maxValue, boolean useSlider) {
        super(name, defaultValue, minValue, maxValue, useSlider,
                String.format("%s.config.option.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }
}
