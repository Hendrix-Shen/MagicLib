package top.hendrixshen.magiclib.impl.malilib;

import fi.dy.masa.malilib.config.options.ConfigInteger;
import fi.dy.masa.malilib.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class TranslatableConfigInteger extends ConfigInteger {
    private final String guiDisplayName;

    public TranslatableConfigInteger(String prefix, String name, int defaultValue) {
        super(name, defaultValue, String.format("%s.config.%s.comment", prefix, name));
        this.guiDisplayName = String.format("%s.config.%s.name", prefix, name);
    }

    public TranslatableConfigInteger(String prefix, String name, int defaultValue, int minValue, int maxValue) {
        super(name, defaultValue, minValue, maxValue, String.format("%s.config.%s.comment", prefix, name));
        this.guiDisplayName = String.format("%s.config.%s.name", prefix, name);
    }

    public TranslatableConfigInteger(String prefix, String name, int defaultValue, int minValue, int maxValue, boolean useSlider) {
        super(name, defaultValue, minValue, maxValue, useSlider, String.format("%s.config.%s.comment", prefix, name));
        this.guiDisplayName = String.format("%s.config.%s.name", prefix, name);
    }

    @Override
    public String getConfigGuiDisplayName() {
        return StringUtils.translate(this.guiDisplayName);
    }
}
