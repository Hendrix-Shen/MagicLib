package top.hendrixshen.magiclib.impl.malilib.config.option;

import fi.dy.masa.malilib.config.options.ConfigInteger;
import lombok.Getter;
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;

@Getter
public class MagicConfigInteger extends ConfigInteger implements MagicIConfigBase {
    private final String translationPrefix;

    public MagicConfigInteger(String translationPrefix, String name, int defaultValue) {
        super(name, defaultValue, String.format("%s.config.option.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }


    public MagicConfigInteger(String translationPrefix, String name, int defaultValue,
                              int minValue, int maxValue) {
        super(name, defaultValue, minValue, maxValue,
                String.format("%s.config.option.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }

    public MagicConfigInteger(String translationPrefix, String name, int defaultValue,
                              int minValue, int maxValue, boolean useSlider) {
        super(name, defaultValue, minValue, maxValue, useSlider,
                String.format("%s.%s.comment", translationPrefix, name));
        this.translationPrefix = translationPrefix;
    }
}
