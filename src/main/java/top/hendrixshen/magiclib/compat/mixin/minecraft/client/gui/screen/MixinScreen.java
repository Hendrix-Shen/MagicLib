package top.hendrixshen.magiclib.compat.mixin.minecraft.client.gui.screen;

import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.client.gui.screens.ScreenCompatApi;

//#if MC <= 11605
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import net.minecraft.client.gui.components.AbstractWidget;
//$$ import net.minecraft.client.gui.components.Widget;
//$$ import net.minecraft.client.gui.components.events.GuiEventListener;
//$$ import java.util.List;
//#endif


@Mixin(Screen.class)
public abstract class MixinScreen implements ScreenCompatApi {

    //#if MC <= 11605
    //$$ @Shadow
    //$$ @Final
    //$$ protected List<AbstractWidget> buttons;

    //$$ @Shadow
    //$$ protected abstract AbstractWidget addButton(AbstractWidget abstractWidget);

    //$$ @Override
    //$$ public GuiEventListener addRenderableWidget(GuiEventListener guiEventListener) {
    //$$     addButton((AbstractWidget) guiEventListener);
    //$$     return guiEventListener;
    //$$ }

    //$$ @Override
    //$$ public Widget addRenderableOnly(Widget widget) {
    //$$     this.buttons.add((AbstractWidget) widget);
    //$$     return widget;
    //$$ }
    //#endif

    //#if MC <= 11502
    //$$ @Shadow
    //$$ @Final
    //$$ protected List<GuiEventListener> children;

    //$$ @Override
    //$$ public GuiEventListener addWidget(GuiEventListener guiEventListener) {
    //$$     this.children.add(guiEventListener);
    //$$     return guiEventListener;
    //$$ }
    //#endif
}
