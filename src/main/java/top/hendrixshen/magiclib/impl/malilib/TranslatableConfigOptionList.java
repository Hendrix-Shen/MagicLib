package top.hendrixshen.magiclib.impl.malilib;

import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.ConfigOptionList;
import fi.dy.masa.malilib.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public class TranslatableConfigOptionList extends ConfigOptionList {
    private final String guiDisplayName;

    public TranslatableConfigOptionList(String prefix, String name, IConfigOptionListEntry defaultValue) {
        super(name, defaultValue, String.format("%s.config.%s.comment", prefix, name),
                String.format("%s.config.%s.pretty_name", prefix, name));
        this.guiDisplayName = String.format("%s.config.%s.name", prefix, name);
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
