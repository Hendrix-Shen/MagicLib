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
import top.hendrixshen.magiclib.impl.malilib.config.gui.SelectorDropDownList;

//#if FABRIC_LIKE
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
//#else
//$$ import org.spongepowered.asm.mixin.injection.ModifyArg;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/mixins/core/gui/element/WidgetDropDownListMixin.java">TweakerMore</a>
 */
@Mixin(value = WidgetDropDownList.class, remap = false)
public class WidgetDropDownListMixin {
    //#if FABRIC_LIKE
    @SuppressWarnings({"ConstantConditions", "PointlessBitwiseExpression"})
    @ModifyArgs(
            method = "render",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(IIIII)V"
            )
    )
    private void selectorDropDownListMakeOpaque(Args args) {
        if ((WidgetDropDownList<?>) (Object) this instanceof SelectorDropDownList<?>) {
            // Ensure background is opaque.
            int bgColor = args.get(4);
            int a = (bgColor >> 24) & 0xFF;
            bgColor = (0xFF << 24) | (a << 16) | (a << 8) | (a << 0);
            args.set(4, bgColor);

            // Show left box border.
            args.set(0, (int) args.get(0) + 1);
        }
    }
    //#else
    //$$ @SuppressWarnings({"ConstantConditions", "PointlessBitwiseExpression"})
    //$$ @ModifyArg(
    //$$         method = "render",
    //$$         at = @At(
    //$$                 value = "INVOKE",
    //$$                 target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(IIIII)V"
    //$$         ),
    //$$         index = 4
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
    //$$                 target = "Lfi/dy/masa/malilib/render/RenderUtils;drawRect(IIIII)V"
    //$$         ),
    //$$         index = 0
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
