package top.hendrixshen.magiclib.compat.mixin.accessor;

import com.mojang.math.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Matrix4f.class)
public interface AccessorMatrix4f {
    @Accessor
    float[] getValues();
}
