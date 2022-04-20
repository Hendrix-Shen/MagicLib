package top.hendrixshen.magiclib.compat.mixin.minecraft.blaze3d.systems;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.renderer.ShaderInstanceCompat;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.annotation.Public;

import java.util.function.Supplier;

@Mixin(RenderSystem.class)
public class MixinRenderSystem {
    @Public
    private static void applyModelViewMatrix() {
    }

    @Public
    private static void setShader(Supplier<ShaderInstanceCompat> supplier) {
    }
}
