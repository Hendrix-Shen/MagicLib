package top.hendrixshen.magiclib.malilib.api.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.language.api.I18n;

import java.util.function.Consumer;

@Environment(EnvType.CLIENT)
public interface IMagicConfigBase extends IConfigBase {
    @Nullable
    Consumer<ConfigBase<?>> getValueChangedFromJsonCallback();

    void setValueChangedFromJsonCallback(@Nullable Consumer<ConfigBase<?>> valueChangedFromJsonCallback);

    String getModIdentifier();

    @Override
    default String getComment() {
        String key = String.format("%s.%s.comment", this.getModIdentifier(), this.getName());
        return I18n.exists(key) ? I18n.get(key) : this.getName();
    }

    @Override
    default String getPrettyName() {
        String key = String.format("%s.%s.pretty_name", this.getModIdentifier(), this.getName());
        return I18n.exists(key) ? I18n.get(key) : StringUtils.splitCamelCase(this.getConfigGuiDisplayName());
    }

    @Override
    default String getConfigGuiDisplayName() {
        String key = String.format("%s.%s.name", this.getModIdentifier(), this.getName());
        return I18n.exists(key) ? I18n.get(key) : this.getName();
    }
}
