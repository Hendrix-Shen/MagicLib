package top.hendrixshen.magiclib.compat.mixin.minecraft.client.gui;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

import java.util.List;

@SuppressWarnings("unused")
@Mixin(Screen.class)
public abstract class MixinScreen {
    @Shadow
    @Final
    protected List<AbstractWidget> buttons;

    @Shadow
    protected abstract AbstractWidget addButton(AbstractWidget abstractWidget);

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

}