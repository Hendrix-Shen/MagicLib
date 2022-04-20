package top.hendrixshen.magiclib.compat.mixin.minecraft.client.renderer;

import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ShaderInstanceCompat;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.annotation.Public;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Public
    @Remap("method_34540")
    private static ShaderInstanceCompat getPositionColorShader() {
        return null;
    }
}
