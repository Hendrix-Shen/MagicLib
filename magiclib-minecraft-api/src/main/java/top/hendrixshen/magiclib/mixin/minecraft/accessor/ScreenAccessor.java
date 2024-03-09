package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

//#if MC > 11605
//$$ import net.minecraft.client.gui.components.Widget;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(Screen.class)
public interface ScreenAccessor {
    @Accessor("children")
    List<GuiEventListener> magiclib$getChildren();

    //#if MC > 11605
    //$$ @Invoker("addRenderableOnly")
    //$$ Widget magiclib$invokeAddRenderableOnly(Widget guiEventListener);
    //$$
    //#if FORGE == 0
    //$$ @Invoker("addRenderableWidget")
    //$$ GuiEventListener magiclib$invokeAddRenderableWidget(GuiEventListener guiEventListener);
    //$$
    //$$ @Invoker("addWidget")
    //$$ GuiEventListener magiclib$invokeAddWidget(GuiEventListener guiEventListener);
    //#endif
    //#else
    @Accessor("buttons")
    List<AbstractWidget> magiclib$getButtons();

    @Invoker("addButton")
    AbstractWidget magiclib$invokeAddButton(AbstractWidget abstractWidget);
    //#endif
}
