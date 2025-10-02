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

package top.hendrixshen.magiclib.mixin.malilib.panel.label;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOption;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOptionBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptions;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;
import top.hendrixshen.magiclib.libs.com.llamalad7.mixinextras.sugar.Local;
import top.hendrixshen.magiclib.mixin.malilib.accessor.WidgetListConfigOptionsAccessor;

import java.util.function.Function;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/f1104ff35c04dd133032de82ec4c779a4a795b60/src/main/java/me/fallenbreath/tweakermore/mixins/core/gui/panel/labelWithOriginalText/WidgetListConfigOptionMixin.java">TweakerMore</a>.
 *
 * <p>
 * We should apply ConfigLabelTextModifier as early as possible.
 * </p>
 */
@Mixin(value = WidgetConfigOption.class, remap = false, priority = 990)
public abstract class WidgetConfigOptionMixin extends WidgetConfigOptionBase<GuiConfigsBase.ConfigOptionWrapper> {
    public WidgetConfigOptionMixin(int x, int y, int width, int height, WidgetListConfigOptionsBase<?, ?> parent,
                                   GuiConfigsBase.ConfigOptionWrapper entry, int listIndex) {
        super(x, y, width, height, parent, entry, listIndex);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @Unique
    private boolean magiclib$isMagicGui() {
        return this.parent instanceof WidgetListConfigOptions
                && ((WidgetListConfigOptionsAccessor) this.parent).magiclib$getParent() instanceof MagicConfigGui;
    }

    @ModifyArg(
            method = "addConfigOption",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/gui/widgets/WidgetConfigOption;addLabel(IIIII[Ljava/lang/String;)V"
            )
    )
    private String[] applyConfigLabelTextModifier(String[] originalLines, @Local(argsOnly = true) IConfigBase config) {
        if (!this.magiclib$isMagicGui() || !(config instanceof MagicIConfigBase) || originalLines == null) {
            return originalLines;
        }

        Function<String, String> modifier = ((MagicIConfigBase) config).getGuiDisplayLineModifier();

        for (int i = 0; i < originalLines.length; i++) {
            originalLines[i] = modifier.apply(originalLines[i]);
        }

        return originalLines;
    }
}
