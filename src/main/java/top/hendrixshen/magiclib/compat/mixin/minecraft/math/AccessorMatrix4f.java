package top.hendrixshen.magiclib.compat.mixin.minecraft.math;

import com.mojang.math.Matrix4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;

//#if MC <= 11404
//$$ import org.spongepowered.asm.mixin.gen.Accessor;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(Matrix4f.class)
public interface AccessorMatrix4f {
    //#if MC <= 11404
    //$$ @Accessor
    //$$ float[] getValues();
    //#endif
}
