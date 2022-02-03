package top.hendrixshen.magiclib.untils.malilib;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigStringList;
import fi.dy.masa.malilib.util.StringUtils;

@SuppressWarnings("unused")
public class TranslatableConfigStringList extends ConfigStringList {
    private final String guiDisplayName;

    public TranslatableConfigStringList(String prefix, String name, ImmutableList<String> defaultValue) {
        super(name, defaultValue, String.format("%s.%s.comment", prefix, name));
        this.guiDisplayName = String.format("%s.%s.name", prefix, name);
    }

    @Override
    public String getConfigGuiDisplayName() {
        return StringUtils.translate(this.guiDisplayName);
    }
}
