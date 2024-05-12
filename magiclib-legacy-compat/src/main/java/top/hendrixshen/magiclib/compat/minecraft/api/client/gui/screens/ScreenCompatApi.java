package top.hendrixshen.magiclib.compat.minecraft.api.client.gui.screens;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
public interface ScreenCompatApi {
    default GuiEventListener addRenderableWidgetCompat(GuiEventListener guiEventListener) {
        throw new UnImplCompatApiException();
    }

    default Widget addRenderableOnlyCompat(Widget widget) {
        throw new UnImplCompatApiException();
    }

    default GuiEventListener addWidgetCompat(GuiEventListener guiEventListener) {
        throw new UnImplCompatApiException();
    }

    //#if MC < 11700
    default GuiEventListener addRenderableWidget(GuiEventListener guiEventListener) {
        return this.addRenderableWidgetCompat(guiEventListener);
    }

    default Widget addRenderableOnly(Widget widget) {
       return this.addRenderableOnlyCompat(widget);
    }
    //#endif

    //#if MC < 11600
    //$$ default GuiEventListener addWidget(GuiEventListener guiEventListener) {
    //$$     return this.addWidgetCompat(guiEventListener);
    //$$ }
    //#endif
}
