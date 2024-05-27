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
import top.hendrixshen.magiclib.api.malilib.config.option.MagicIConfigBase;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;
import top.hendrixshen.magiclib.mixin.malilib.accessor.WidgetListConfigOptionsAccessor;

import java.util.function.Function;

//#if FABRIC_LIKE
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
//#else
//$$ import org.jetbrains.annotations.Nullable;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/mixins/core/gui/panel/dropDownListRedraw/WidgetListBaseMixin.java">TweakerMore<a/>
 */
@Mixin(value = WidgetConfigOption.class, remap = false)
public abstract class WidgetConfigOptionMixin extends WidgetConfigOptionBase<GuiConfigsBase.ConfigOptionWrapper> {
    public WidgetConfigOptionMixin(int x, int y, int width, int height, WidgetListConfigOptionsBase<?, ?> parent,
                                   GuiConfigsBase.ConfigOptionWrapper entry, int listIndex) {
        super(x, y, width, height, parent, entry, listIndex);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @Unique
    private boolean magiclib$isMagicGui() {
        return this.parent instanceof WidgetListConfigOptions &&
                ((WidgetListConfigOptionsAccessor) this.parent).magiclib$getParent() instanceof MagicConfigGui;
    }

    //#if FABRIC_LIKE
    @ModifyArgs(
            method = "addConfigOption",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/gui/widgets/WidgetConfigOption;addLabel(IIIII[Ljava/lang/String;)V"
            )
    )
    private void applyConfigLabelTextModifier(Args args, int x_, int y_, float zLevel, int labelWidth,
                                              int configWidth, IConfigBase config) {
        if (!this.magiclib$isMagicGui()) {
            return;
        }

        if (!(config instanceof MagicIConfigBase)) {
            return;
        }

        String[] originalLines = args.get(5);
        Function<String, String> modifier = ((MagicIConfigBase) config).getGuiDisplayLineModifier();

        for (int i = 0; i < originalLines.length; i++) {
            originalLines[i] = modifier.apply(originalLines[i]);
        }
    }
    //#else
    //$$ @Unique
    //$$ private MagicIConfigBase magiclib$interceptedConfig = null;
    //$$
    //$$ @Inject(
    //$$         method = "addConfigOption",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lfi/dy/masa/malilib/gui/widgets/WidgetConfigOption;addLabel(IIIII[Ljava/lang/String;)V"
    //$$         )
    //$$ )
    //$$ private void interceptConfig(int x, int y, float zLevel, int labelWidth, int configWidth,
    //$$                              IConfigBase config, CallbackInfo ci) {
    //$$     if (!this.magiclib$isMagicGui()) {
    //$$         return;
    //$$     }
    //$$
    //$$     if (!(config instanceof MagicIConfigBase)) {
    //$$         return;
    //$$     }
    //$$
    //$$     this.magiclib$interceptedConfig = (MagicIConfigBase) config;
    //$$ }
    //$$
    //$$ @ModifyArg(
    //$$         method = "addConfigOption",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lfi/dy/masa/malilib/gui/widgets/WidgetConfigOption;addLabel(IIIII[Ljava/lang/String;)V"
    //$$         )
    //$$ )
    //$$ private String @Nullable [] applyConfigLabelTextModifier(String[] originalLines) {
    //$$     if (this.magiclib$interceptedConfig != null) {
    //$$         Function<String, String> modifier = this.magiclib$interceptedConfig.getGuiDisplayLineModifier();
    //$$
    //$$         for (int i = 0; i < originalLines.length; i++) {
    //$$             originalLines[i] = modifier.apply(originalLines[i]);
    //$$         }
    //$$     }
    //$$
    //$$     return originalLines;
    //$$ }
    //#endif
}
