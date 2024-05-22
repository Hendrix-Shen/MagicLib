package top.hendrixshen.magiclib.api.malilib.config.option;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiBase;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.impl.malilib.config.ConfigContainer;
import top.hendrixshen.magiclib.impl.malilib.config.GlobalConfigManager;
import top.hendrixshen.magiclib.util.minecraft.StringUtil;

import java.util.function.Function;

public interface MagicIConfigBase extends IConfigBase {
    String getTranslationPrefix();

    void onValueChanged(boolean fromFile);

    @Override
    default String getComment() {
        return StringUtil.translateOrFallback(String.format("%s.config.option.%s.comment",
                this.getTranslationPrefix(), this.getName()), null);
    }

    @Override
    default String getPrettyName() {
        return StringUtil.translateOrFallback(String.format("%s.config.option.%s.name",
                this.getTranslationPrefix(), this.getName()), this.getConfigGuiDisplayName());
    }

    @Override
    default String getConfigGuiDisplayName() {
        return StringUtil.translateOrFallback(String.format("%s.config.option.%s.name",
                this.getTranslationPrefix(), this.getName()), this.getName());
    }

    default void updateStatisticOnUse() {
        this.getMagicContainer().getStatistic().onConfigUsed();
    }

    default void setCommentModifier(@Nullable Function<String, String> commentModifier) {
        this.getMagicContainer().setCommentModifier(commentModifier);
    }

    default String getCommentNoFooter() {
        ConfigContainer option = this.getMagicContainer();
        option.setAppendFooterFlag(false);

        try {
            return this.getComment();
        } finally {
            option.setAppendFooterFlag(true);
        }
    }

    default ConfigContainer getMagicContainer() {
        return GlobalConfigManager.getInstance().getContainerByConfig(this).orElseThrow(() -> new RuntimeException(""));
    }

    default Function<String, String> getGuiDisplayLineModifier() {
        ConfigContainer magicContainer = this.getMagicContainer();

        if (!magicContainer.isSatisfied()) {
            return line -> GuiBase.TXT_DARK_RED + line + GuiBase.TXT_RST;
        } else if (magicContainer.isDebugOnly()) {
            return line -> GuiBase.TXT_BLUE + line + GuiBase.TXT_RST;
        } else if (magicContainer.isDevOnly()) {
            return line -> GuiBase.TXT_LIGHT_PURPLE + line + GuiBase.TXT_RST;
        }

        return line -> line;
    }
}
