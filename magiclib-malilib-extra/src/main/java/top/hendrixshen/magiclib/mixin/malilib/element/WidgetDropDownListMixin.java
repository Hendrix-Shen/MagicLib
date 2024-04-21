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
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
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
