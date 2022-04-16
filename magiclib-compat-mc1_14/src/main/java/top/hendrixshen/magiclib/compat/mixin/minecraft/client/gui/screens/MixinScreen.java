package top.hendrixshen.magiclib.compat.mixin.minecraft.client.gui.screens;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.compat.annotation.Remap;

import java.util.List;

@SuppressWarnings("unused")
@Mixin(Screen.class)
public abstract class MixinScreen {
    @Shadow
    @Final
    protected List<GuiEventListener> children;
    @Shadow
    @Final
    protected List<AbstractWidget> buttons;

    @Shadow
    protected abstract AbstractWidget addButton(AbstractWidget abstractWidget);

    @Shadow
    protected abstract void init();

    @Remap("method_37063")
    protected GuiEventListener addRenderableWidget(GuiEventListener guiEventListener) {
        addButton((AbstractWidget) guiEventListener);
        return guiEventListener;
    }

    @Remap("method_37060")
    protected Widget addRenderableOnly(Widget widget) {
        this.buttons.add((AbstractWidget) widget);
        return widget;
    }

    @Remap("method_25429")
    protected GuiEventListener addWidget(GuiEventListener guiEventListener) {
        this.children.add(guiEventListener);
        return guiEventListener;
    }

    @Inject(method = "init()V", at = @At(value = "HEAD"))
    private void preInit(CallbackInfo ci) {
        remap$init();
    }

    @Remap("method_25426")
    protected void remap$init() {
    }
}
