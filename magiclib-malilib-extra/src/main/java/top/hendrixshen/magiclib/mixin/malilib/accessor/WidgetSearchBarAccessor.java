package top.hendrixshen.magiclib.mixin.malilib.accessor;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetSearchBar;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(value = WidgetSearchBar.class, remap = false)
public interface WidgetSearchBarAccessor {
    @Accessor("searchBox")
    GuiTextFieldGeneric magiclib$getSearchBox();
}
