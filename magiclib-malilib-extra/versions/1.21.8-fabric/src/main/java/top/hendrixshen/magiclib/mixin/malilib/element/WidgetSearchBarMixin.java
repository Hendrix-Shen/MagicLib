package top.hendrixshen.magiclib.mixin.malilib.element;

import fi.dy.masa.malilib.gui.GuiTextFieldGeneric;
import fi.dy.masa.malilib.gui.widgets.WidgetSearchBar;

import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import top.hendrixshen.magiclib.api.fake.malilib.WidgetSearchBarOpenStateAccessor;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.21.6+        : subproject 1.21.8        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(value = WidgetSearchBar.class, remap = false)
public abstract class WidgetSearchBarMixin implements WidgetSearchBarOpenStateAccessor {
    @Shadow
    protected boolean searchOpen;

    @Shadow
    @Final
    protected GuiTextFieldGeneric searchBox;

    /**
     * It is currently unknown whether moving the mouse freely will cause side effects,
     * so reverting to the legacy version's behavior.
     */
    @Override
    public void magiclib$setSearchOpen(boolean isOpen) {
        this.searchOpen = isOpen;

        if (this.searchOpen) {
            this.searchBox.setFocused(true);
        }
    }
}
