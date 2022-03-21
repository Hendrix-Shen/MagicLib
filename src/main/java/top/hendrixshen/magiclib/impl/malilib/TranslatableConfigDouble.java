package top.hendrixshen.magiclib.impl.malilib;

import fi.dy.masa.malilib.config.options.ConfigDouble;
import fi.dy.masa.malilib.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class TranslatableConfigDouble extends ConfigDouble {
    private final String guiDisplayName;

    public TranslatableConfigDouble(String prefix, String name, double defaultValue) {
        super(name, defaultValue, String.format("%s.%s.comment", prefix, name));
        this.guiDisplayName = String.format("%s.%s.name", prefix, name);
    }

    public TranslatableConfigDouble(String prefix, String name, double defaultValue, double minValue, double maxValue) {
        super(name, defaultValue, minValue, maxValue, String.format("%s.%s.comment", prefix, name));
        this.guiDisplayName = String.format("%s.%s.name", prefix, name);
    }

    public TranslatableConfigDouble(String prefix, String name, double defaultValue, double minValue, double maxValue, boolean useSlider) {
        super(name, defaultValue, minValue, maxValue, useSlider, String.format("%s.%s.comment", prefix, name));
        this.guiDisplayName = String.format("%s.%s.name", prefix, name);
    }

    @Override
    public String getPrettyName() {
        String ret = super.getPrettyName();
        if (ret.contains("pretty_name")) {
            ret = StringUtils.splitCamelCase(this.getConfigGuiDisplayName());
        }
        return ret;
    }

    @Override
    public String getConfigGuiDisplayName() {
        return StringUtils.translate(this.guiDisplayName);
    }
}
