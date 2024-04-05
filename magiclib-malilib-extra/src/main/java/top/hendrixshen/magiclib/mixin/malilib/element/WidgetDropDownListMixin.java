package top.hendrixshen.magiclib.mixin.malilib.element;

import fi.dy.masa.malilib.gui.widgets.WidgetDropDownList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import top.hendrixshen.magiclib.impl.malilib.config.gui.SelectorDropDownList;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
 */
@Mixin(value = WidgetDropDownList.class, remap = false)
public class WidgetDropDownListMixin {
    // FIXME: ModifyArgs broken on Forge 1.17+
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
