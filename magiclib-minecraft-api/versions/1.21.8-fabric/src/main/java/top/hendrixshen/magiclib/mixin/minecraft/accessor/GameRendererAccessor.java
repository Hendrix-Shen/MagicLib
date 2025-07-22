package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.renderer.GameRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.21.6+        : subproject 1.21.8        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
    @Accessor("guiRenderState")
    GuiRenderState magiclib$getGuiRenderState();
}
