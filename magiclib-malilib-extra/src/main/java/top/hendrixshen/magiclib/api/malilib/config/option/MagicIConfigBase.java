package top.hendrixshen.magiclib.api.malilib.config.option;

import fi.dy.masa.malilib.config.IConfigBase;
import top.hendrixshen.magiclib.impl.malilib.config.ConfigContainer;
import top.hendrixshen.magiclib.impl.malilib.config.GlobalConfigManager;
import top.hendrixshen.magiclib.util.minecraft.StringUtil;

public interface MagicIConfigBase extends IConfigBase {
    String getTranslationPrefix();

    @Override
    default String getComment() {
        return StringUtil.translateOrFallback(String.format("%s.config.option.%s.comment",
                this.getTranslationPrefix(), this.getName()), null);
    }

    @Override
    default String getPrettyName() {
        return StringUtil.translateOrFallback(String.format("%s.config.option.%s.pretty_name",
                this.getTranslationPrefix(), this.getName()), this.getName());
    }

    @Override
    default String getConfigGuiDisplayName() {
        return StringUtil.translateOrFallback(String.format("%s.config.option.%s.name",
                this.getTranslationPrefix(), this.getName()), this.getName());
    }

    default ConfigContainer getMagicContainer() {
        return GlobalConfigManager.getInstance().getContainerByConfig(this).orElseThrow(() -> new RuntimeException(""));
    }
}
