package top.hendrixshen.magiclib.compat.minecraft.mixin.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.client.gui.screens.ScreenCompatApi;

//#if MC <= 11605
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import net.minecraft.client.gui.components.AbstractWidget;
//$$ import java.util.List;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public abstract class MixinScreen implements ScreenCompatApi {
    //#if MC > 11605
    @SuppressWarnings("target")
    @Shadow
    protected abstract GuiEventListener addRenderableWidget(GuiEventListener guiEventListener);

    @Shadow
    protected abstract Renderable addRenderableOnly(Renderable widget);

    //#else
    //$$ @Shadow
    //$$ @Final
    //$$ protected List<AbstractWidget> buttons;
    //$$
    //$$
    //$$ @Shadow
    //$$ protected abstract AbstractWidget addButton(AbstractWidget abstractWidget);
    //#endif
    @Override
    public GuiEventListener addRenderableWidgetCompat(GuiEventListener guiEventListener) {
        //#if MC > 11605
        return this.addRenderableWidget(guiEventListener);
        //#else
        //$$ addButton((AbstractWidget) guiEventListener);
        //$$ return guiEventListener;
        //#endif
    }

    @Override
    public Renderable addRenderableOnlyCompat(Renderable widget) {
        //#if MC > 11605
        return this.addRenderableOnly(widget);
        //#else
        //$$ this.buttons.add((AbstractWidget) widget);
        //$$ return widget;
        //#endif
    }

    //#if MC > 11502
    @SuppressWarnings("target")
    @Shadow
    protected abstract GuiEventListener addWidget(GuiEventListener guiEventListener);
    //#else
    //$$ @Shadow
    //$$ @Final
    //$$ protected List<GuiEventListener> children;
    //#endif

    @Override
    public GuiEventListener addWidgetCompat(GuiEventListener guiEventListener) {
        //#if MC > 11502
        return this.addWidget(guiEventListener);
        //#else
        //$$ this.children.add(guiEventListener);
        //$$ return guiEventListener;
        //#endif
    }
}
