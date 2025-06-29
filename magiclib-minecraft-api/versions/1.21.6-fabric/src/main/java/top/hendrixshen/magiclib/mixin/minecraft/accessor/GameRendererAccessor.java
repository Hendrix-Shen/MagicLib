package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.renderer.GameRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// Used in mc1.21.6+
@Mixin(GameRenderer.class)
public interface GameRendererAccessor {
    @Accessor("guiRenderState")
    GuiRenderState magiclib$getGuiRenderState();
}
