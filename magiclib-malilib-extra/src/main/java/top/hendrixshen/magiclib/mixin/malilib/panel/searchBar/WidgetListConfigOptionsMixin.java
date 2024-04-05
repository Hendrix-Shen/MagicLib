package top.hendrixshen.magiclib.mixin.malilib.panel.searchBar;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.widgets.WidgetConfigOption;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptions;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptionsBase;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.impl.malilib.config.gui.MagicConfigGui;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/TweakerMore">TweakerMore</a>
 */
@Mixin(value = WidgetListConfigOptions.class, remap = false)
public abstract class WidgetListConfigOptionsMixin extends WidgetListConfigOptionsBase<GuiConfigsBase.ConfigOptionWrapper, WidgetConfigOption> {
    @Shadow
    @Final
    protected GuiConfigsBase parent;

    public WidgetListConfigOptionsMixin(int x, int y, int width, int height, int configWidth) {
        super(x, y, width, height, configWidth);
    }

    @ModifyArg(
            method = "<init>",
            at = @At(
                    value = "INVOKE",
                    target = "Lfi/dy/masa/malilib/gui/widgets/WidgetSearchBar;<init>(IIIIILfi/dy/masa/malilib/gui/interfaces/IGuiIcon;Lfi/dy/masa/malilib/gui/LeftRight;)V"
            ),
            index = 2
    )
    private int magiclibSearchBarWidth(int width) {
        if (this.parent instanceof MagicConfigGui) {
            // A default value.
            // A more precise width control wil be applied during the initGui of MagicConfigGui
            width -= 150;
        }

        return width;
    }

    @Inject(
            method = "<init>",
            at = @At(
                    value = "TAIL"
            )
    )
    private void magiclibRecordSearchBar(CallbackInfo ci) {
        if (this.parent instanceof MagicConfigGui) {
            ((MagicConfigGui) this.parent).setSearchBar(this.widgetSearchBar);
        }
    }
}
