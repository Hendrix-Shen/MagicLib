package top.hendrixshen.magiclib.impl.malilib;

import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class TranslatableConfigBoolean extends ConfigBoolean {
    private final String guiDisplayName;

    public TranslatableConfigBoolean(String prefix, String name, boolean defaultValue) {
        super(name, defaultValue, String.format("%s.config.%s.comment", prefix, name),
                String.format("%s.config.%s.pretty_name", prefix, name));
        this.guiDisplayName = String.format("%s.config.%s.name", prefix, name);
    }

    @Override
    public String getConfigGuiDisplayName() {
        return StringUtils.translate(this.guiDisplayName);
    }
}