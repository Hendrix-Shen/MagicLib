package top.hendrixshen.magiclib.mixin.malilib.panel.label;

import fi.dy.masa.malilib.config.IConfigBase;
import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOption;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptions;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;

import java.util.List;
import java.util.Objects;

@Mixin(value = WidgetListConfigOptions.class, remap = false)
public abstract class WidgetListConfigOptionsMixin extends WidgetListConfigOptionsBase<GuiConfigsBase.ConfigOptionWrapper, WidgetConfigOption> {
    @Shadow
    @Final
    protected GuiConfigsBase parent;

    public WidgetListConfigOptionsMixin(int x, int y, int width, int height, int configWidth) {
        super(x, y, width, height, configWidth);
    }

    // Use getConfigGuiDisplayName instead of getName
    @Inject(
            method = "getMaxNameLengthWrapped",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/List;iterator()Ljava/util/Iterator;"
            ),
            cancellable = true
    )
    private void fixLabelWidth(List<GuiConfigsBase.ConfigOptionWrapper> wrappers, CallbackInfoReturnable<Integer> cir) {
        if (this.parent instanceof MagicConfigGui) {
            int maxWidth = 0;

            for (GuiConfigsBase.ConfigOptionWrapper wrapper : wrappers) {
                if (wrapper.getType() == GuiConfigsBase.ConfigOptionWrapper.Type.CONFIG) {
                    maxWidth = Math.max(maxWidth, this.getStringWidth(
                            Objects.requireNonNull(wrapper.getConfig()).getConfigGuiDisplayName()));
                }
            }

            cir.setReturnValue(maxWidth);
        }
    }
}
