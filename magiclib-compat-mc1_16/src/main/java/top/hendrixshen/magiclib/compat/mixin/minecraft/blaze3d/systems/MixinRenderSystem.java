package top.hendrixshen.magiclib.compat.mixin.minecraft.blaze3d.systems;

import com.mojang.blaze3d.systems.RenderSystem;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.annotation.Public;

@Mixin(RenderSystem.class)
public class MixinRenderSystem {
    @Public
    private static void applyModelViewMatrix() {
    }

}
