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

package top.hendrixshen.magiclib.mixin.malilib.element;

import fi.dy.masa.malilib.gui.widgets.WidgetDropDownList;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

// CHECKSTYLE.OFF: ImportOrder
//#if FABRIC_LIKE
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
//#else
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.malilib.config.gui.SelectorDropDownList;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/ae01c423f14ad6d3e45527bfe9450191ba19bd35/src/main/java/me/fallenbreath/tweakermore/mixins/core/gui/element/WidgetDropDownListMixin.java">TweakerMore</a>.
 */
@Mixin(value = WidgetDropDownList.class, remap = false)
public abstract class WidgetDropDownListMixin {
    //#if FABRIC_LIKE
    @SuppressWarnings({"ConstantConditions", "PointlessBitwiseExpression", "PointlessArithmeticExpression"})
    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    //#if MC >= 12111
                    //$$ target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(Lfi/dy/masa/malilib/render/GuiContext;IIIII)V"
                    //#elseif MC >= 12106
                    //$$ target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(Lnet/minecraft/client/gui/GuiGraphics;IIIII)V",
                    //$$ remap = true
                    //#elseif MC > 12104
                    //$$ target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(IIIIIZ)V"
                    //#else
                    target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(IIIII)V"
                    //#endif
            )
    )
    private void selectorDropDownListMakeOpaque(Args args) {
        if ((WidgetDropDownList<?>) (Object) this instanceof SelectorDropDownList<?>) {
            //#if MC >= 12106
            //$$ final int baseIdx = 1;  // the 1st guiGraphics param
            //#else
            final int baseIdx = 0;
            //#endif

            // Ensure background is opaque.
            int bgColor = args.get(baseIdx + 4);
            int a = (bgColor >> 24) & 0xFF;
            bgColor = (0xFF << 24) | (a << 16) | (a << 8) | (a << 0);
            args.set(baseIdx + 4, bgColor);

            // Show left box border.
            args.set(baseIdx + 0, (int) args.get(baseIdx + 0) + 1);
        }
    }
    //#else
    //$$ @SuppressWarnings({"ConstantConditions", "PointlessBitwiseExpression"})
    //$$ @ModifyArg(
    //$$         method = "render",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //#if MC >= 12111
    //$$                 target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(Lfi/dy/masa/malilib/render/GuiContext;IIIII)V"
    //#elseif MC >= 12106
    //$$                 target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(Lnet/minecraft/client/gui/GuiGraphics;IIIII)V",
    //$$                 remap = true
    //#elseif MC > 12104
    //$$                 target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(IIIIIZ)V"
    //#else
    //$$                 target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(IIIII)V"
    //#endif
    //$$         ),
    //#if MC >= 12106
    //$$         index = 5
    //#else
    //$$         index = 4
    //#endif
    //$$ )
    //$$ private int makeOpaque(int alpha) {
    //$$     if ((WidgetDropDownList<?>) (Object) this instanceof SelectorDropDownList<?>) {
    //$$         // Ensure background is opaque.
    //$$         int a = (alpha >> 24) & 0xFF;
    //$$         alpha = (0xFF << 24) | (a << 16) | (a << 8) | (a << 0);
    //$$     }
    //$$
    //$$     return alpha;
    //$$ }
    //$$
    //$$ @SuppressWarnings({"ConstantConditions"})
    //$$ @ModifyArg(
    //$$         method = "render",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //#if MC >= 12111
    //$$                 target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(Lfi/dy/masa/malilib/render/GuiContext;IIIII)V"
    //#elseif MC >= 12106
    //$$                 target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(Lnet/minecraft/client/gui/GuiGraphics;IIIII)V",
    //$$                 remap = true
    //#elseif MC > 12104
    //$$                 target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(IIIIIZ)V"
    //#else
    //$$                 target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(IIIII)V"
    //#endif
    //$$         ),
    //#if MC >= 12106
    //$$         index = 5
    //#else
    //$$         index = 4
    //#endif
    //$$ )
    //$$ private int showBorder(int x) {
    //$$     if ((WidgetDropDownList<?>) (Object) this instanceof SelectorDropDownList<?>) {
    //$$         x++;
    //$$     }
    //$$
    //$$     return x;
    //$$ }
    //#endif

    @SuppressWarnings("ConstantConditions")
    @Inject(
            method = "onMouseScrolledImpl",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/gui/GuiScrollBar;offsetValue(I)V",
                    shift = At.Shift.AFTER
            ),
            cancellable = true
    )
    private void fixNoReturnValueHandlingForScroll(CallbackInfoReturnable<Boolean> cir) {
        if ((WidgetDropDownList<?>) (Object) this instanceof SelectorDropDownList<?>) {
            cir.setReturnValue(true);
        }
    }
}
