package top.hendrixshen.magiclib.compat.minecraft.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;

@Environment(EnvType.CLIENT)
public interface ScreenCompatApi {
    default GuiEventListener addRenderableWidgetCompat(GuiEventListener guiEventListener) {
        throw new UnsupportedOperationException();
    }

    default Renderable addRenderableOnlyCompat(Renderable widget) {
        throw new UnsupportedOperationException();
    }

    default GuiEventListener addWidgetCompat(GuiEventListener guiEventListener) {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11605
    //$$ default GuiEventListener addRenderableWidget(GuiEventListener guiEventListener) {
    //$$     return this.addRenderableWidgetCompat(guiEventListener);
    //$$ }
    //$$
    //$$ default Widget addRenderableOnly(Widget widget) {
    //$$    return this.addRenderableOnlyCompat(widget);
    //$$ }
    //#endif

    //#if MC <= 11502
    //$$ default GuiEventListener addWidget(GuiEventListener guiEventListener) {
    //$$     return this.addWidgetCompat(guiEventListener);
    //$$ }
    //#endif
}
