package top.hendrixshen.magiclib.malilib.api.config;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.config.options.ConfigBase;
import fi.dy.masa.malilib.util.StringUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.language.api.I18n;
import top.hendrixshen.magiclib.util.minecraft.StringUtil;

import java.util.function.Consumer;

@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
public interface IMagicConfigBase extends IConfigBase {
    @Nullable
    Consumer<ConfigBase<?>> getValueChangedFromJsonCallback();

    void setValueChangedFromJsonCallback(@Nullable Consumer<ConfigBase<?>> valueChangedFromJsonCallback);

    String getPrefix();

    @Override
    default String getComment() {
        return StringUtil.translateOrFallback(String.format("%s.%s.comment", this.getPrefix(), this.getName()), this.getName());
    }

    @Override
    default String getPrettyName() {
        return StringUtil.translateOrFallback(String.format("%s.%s.pretty_name", this.getPrefix(), this.getName()), this.getName());
    }

    @Override
    default String getConfigGuiDisplayName() {
        return StringUtil.translateOrFallback(String.format("%s.%s.name", this.getPrefix(), this.getName()), this.getName());
    }
}
