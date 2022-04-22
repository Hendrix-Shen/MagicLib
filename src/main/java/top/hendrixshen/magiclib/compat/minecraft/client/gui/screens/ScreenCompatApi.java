package top.hendrixshen.magiclib.compat.minecraft.client.gui.screens;

//#if MC <= 11605
//$$ import net.minecraft.client.gui.components.Widget;
//$$ import net.minecraft.client.gui.components.events.GuiEventListener;
//#endif

public interface ScreenCompatApi {
    //#if MC <= 11605
    //$$ default GuiEventListener addRenderableWidget(GuiEventListener guiEventListener) {
    //$$     throw new UnsupportedOperationException();
    //$$ }

    //$$ default Widget addRenderableOnly(Widget widget) {
    //$$     throw new UnsupportedOperationException();
    //$$ }
    //#endif

    //#if MC <= 11502
    //$$ default GuiEventListener addWidget(GuiEventListener guiEventListener) {
    //$$     throw new UnsupportedOperationException();
    //$$ }
    //#endif
}
