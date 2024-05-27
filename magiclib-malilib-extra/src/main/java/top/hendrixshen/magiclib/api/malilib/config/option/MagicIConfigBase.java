/*
 * This file is part of the TweakerMore project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * TweakerMore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TweakerMore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TweakerMore.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.hendrixshen.magiclib.api.malilib.config.option;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiBase;
import org.jetbrains.annotations.Nullable;
import top.hendrixshen.magiclib.impl.malilib.config.ConfigContainer;
import top.hendrixshen.magiclib.impl.malilib.config.GlobalConfigManager;
import top.hendrixshen.magiclib.util.minecraft.StringUtil;

import java.util.function.Function;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/config/options/TweakerMoreIConfigBase.java">TweakerMore<a/>
 */
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
        return GlobalConfigManager.getInstance().getContainerByConfig(this)
                .orElseThrow(() -> new RuntimeException("Failed to fetch ConfigContainer"));
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
