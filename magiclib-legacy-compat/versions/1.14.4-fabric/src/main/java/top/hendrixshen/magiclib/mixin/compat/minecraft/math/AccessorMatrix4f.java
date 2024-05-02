package top.hendrixshen.magiclib.mixin.compat.minecraft.math;

import com.mojang.math.Matrix4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

/**
 * The implementation for mc [1.14.4, ~)
 */
@Environment(EnvType.CLIENT)
@Mixin(Matrix4f.class)
public interface AccessorMatrix4f {
    @Accessor
    float[] getValues();
}
