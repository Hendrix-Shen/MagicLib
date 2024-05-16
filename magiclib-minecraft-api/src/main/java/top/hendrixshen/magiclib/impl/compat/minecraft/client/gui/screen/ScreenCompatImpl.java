package top.hendrixshen.magiclib.impl.compat.minecraft.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.client.gui.screen.ScreenCompat;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.ScreenAccessor;

//#if MC < 11700 || FORGE_LIKE
import net.minecraft.client.gui.components.AbstractWidget;
//#endif

@Environment(EnvType.CLIENT)
public class ScreenCompatImpl extends AbstractCompat<Screen> implements ScreenCompat {
    public ScreenCompatImpl(@NotNull Screen type) {
        super(type);
    }

    @Override
    public GuiEventListener addRenderableWidget(GuiEventListener guiEventListener) {
        //#if MC > 11605
        //#if FORGE
        //$$ return this.get().addRenderableWidget((AbstractWidget) guiEventListener);
        //#else
        //$$ return ((ScreenAccessor) this.get()).magiclib$invokeAddRenderableWidget(guiEventListener);
        //#endif
        //#else
        return ((ScreenAccessor) this.get()).magiclib$invokeAddButton((AbstractWidget) guiEventListener);
        //#endif
    }

    @Override
    public Widget addRenderableOnly(Widget widget) {
        //#if MC > 11605
        //$$ return ((ScreenAccessor) this.get()).magiclib$invokeAddRenderableOnly(widget);
        //#else
        ((ScreenAccessor) this.get()).magiclib$getButtons().add((AbstractWidget) widget);
        return widget;
        //#endif
    }

    @Override
    public GuiEventListener addWidget(GuiEventListener guiEventListener) {
        //#if MC > 11605
        //#if FORGE
        //$$ return this.get().addWidget((AbstractWidget) guiEventListener);
        //#else
        //$$ return ((ScreenAccessor) this.get()).magiclib$invokeAddWidget(guiEventListener);
        //#endif
        //#else
        ((ScreenAccessor) this.get()).magiclib$getChildren().add(guiEventListener);
        return guiEventListener;
        //#endif

    }

    @Override
    public GuiEventListener addButton(GuiEventListener guiEventListener) {
        return this.addRenderableWidget(guiEventListener);
    }
}
