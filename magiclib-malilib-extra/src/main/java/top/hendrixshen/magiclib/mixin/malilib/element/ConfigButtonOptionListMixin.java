package top.hendrixshen.magiclib.mixin.malilib.element;

import fi.dy.masa.malilib.config.IConfigOptionList;
import fi.dy.masa.malilib.gui.button.ButtonGeneric;
import fi.dy.masa.malilib.gui.button.ConfigButtonOptionList;
import fi.dy.masa.malilib.gui.interfaces.IGuiIcon;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.api.malilib.config.gui.ConfigButtonOptionListHovering;

@Mixin(value = ConfigButtonOptionList.class, remap = false)
public abstract class ConfigButtonOptionListMixin extends ButtonGeneric implements ConfigButtonOptionListHovering {
    @Shadow
    @Final
    private IConfigOptionList config;

    @Shadow
    public abstract void updateDisplayString();

    @Unique
    private boolean magiclib$enableValueHovering;

    public ConfigButtonOptionListMixin(int x, int y, IGuiIcon icon, String... hoverStrings) {
        super(x, y, icon, hoverStrings);
    }

    @Override
    public void magiclib$setEnableValueHovering() {
        this.magiclib$enableValueHovering = true;
        this.updateDisplayString();
    }

    @Inject(
            method = "updateDisplayString",
            at = @At(
                    value = "TAIL"
            )
    )
    private void postUpdateDisplayString(CallbackInfo ci) {
        if (this.magiclib$enableValueHovering) {
            this.setHoverStrings(this.makeHoveringLines(this.config));
        }
    }
}
