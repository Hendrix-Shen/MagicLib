package top.hendrixshen.magiclib.mixin.malilib.accessor;

import fi.dy.masa.malilib.gui.GuiConfigsBase;
import fi.dy.masa.malilib.gui.widgets.WidgetListConfigOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = WidgetListConfigOptions.class, remap = false)
public interface WidgetListConfigOptionsAccessor {
    @Accessor("parent")
    GuiConfigsBase magiclib$getParent();
}
