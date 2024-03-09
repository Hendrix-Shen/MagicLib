package top.hendrixshen.magiclib.api.compat.minecraft.client.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.client.gui.screen.ScreenCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

@Environment(EnvType.CLIENT)
public interface ScreenCompat extends Provider<Screen> {
    static @NotNull ScreenCompat of(@NotNull Screen screen) {
        return new ScreenCompatImpl(screen);
    }

    GuiEventListener addRenderableWidget(GuiEventListener guiEventListener);

    Widget addRenderableOnly(Widget widget);

    GuiEventListener addWidget(GuiEventListener guiEventListener);

    GuiEventListener addButton(GuiEventListener guiEventListener);
}
