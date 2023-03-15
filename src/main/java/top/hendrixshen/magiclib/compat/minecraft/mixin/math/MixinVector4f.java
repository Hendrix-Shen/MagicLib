package top.hendrixshen.magiclib.compat.minecraft.mixin.math;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.api.math.Vector4fCompatApi;

//#if MC > 11902
import top.hendrixshen.magiclib.compat.preprocess.api.DummyClass;
//#else
//$$ import com.mojang.math.Matrix4f;
//$$ import com.mojang.math.Vector4f;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif

@Environment(EnvType.CLIENT)
//#if MC > 11903
@Mixin(DummyClass.class)
//#else
//$$ @Mixin(Vector4f.class)
//#endif
public abstract class MixinVector4f implements Vector4fCompatApi {
    //#if MC < 11903
    //#if MC > 11404
    //$$ @Shadow
    //$$ public abstract void transform(Matrix4f matrix4f);
    //#endif
    //$$
    //$$ @Override
    //$$ public void transformCompat(Matrix4f matrix4f) {
    //#if MC > 11404
    //$$     this.transform(matrix4f);
    //#else
    //$$     Vector4f vector4f = (Vector4f) (Object) this;
    //$$     float[] values = ((AccessorMatrix4f) (Object) matrix4f).getValues();
    //$$     float vx = vector4f.x();
    //$$     float vy = vector4f.y();
    //$$     float vz = vector4f.z();
    //$$     float vw = vector4f.w();
    //$$     float x = values[0] * vx + values[4] * vy + values[8] * vz + values[12] * vw;
    //$$     float y = values[1] * vx + values[5] * vy + values[9] * vz + values[13] * vw;
    //$$     float z = values[2] * vx + values[6] * vy + values[10] * vz + values[14] * vw;
    //$$     float w = values[3] * vx + values[7] * vy + values[11] * vz + values[15] * vw;
    //$$     vector4f.set(x, y, z, w);
    //#endif
    //$$ }
    //#endif
}