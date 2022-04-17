package top.hendrixshen.magiclib.compat.mixin.minecraft.util;

import net.minecraft.util.Mth;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.annotation.Public;
import top.hendrixshen.magiclib.compat.annotation.Remap;
import top.hendrixshen.magiclib.compat.util.MthCompatApi;

@Mixin(Mth.class)
public class MixinMth {
    @Public
    @Remap("method_23278")
    private static float fastInvCubeRootCompat(float f) {
        return MthCompatApi.fastInvCubeRootCompat(f);
    }
}
