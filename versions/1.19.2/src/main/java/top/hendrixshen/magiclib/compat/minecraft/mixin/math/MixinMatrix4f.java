package top.hendrixshen.magiclib.compat.minecraft.mixin.math;

import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.math.Matrix4fCompatApi;

//#if MC < 11500
//$$ import org.spongepowered.asm.mixin.Final;
//#endif

/**
 * The implementation for mc [1.14.4, ~)
 */
@Environment(EnvType.CLIENT)
@Mixin(Matrix4f.class)
public abstract class MixinMatrix4f implements Matrix4fCompatApi {
    //#if MC > 11605
    @Shadow
    public abstract void multiplyWithTranslation(float f, float g, float h);
    //#endif

    //#if MC > 11404
    @Shadow
    public abstract void setIdentity();

    @Shadow
    public abstract void multiply(Matrix4f matrix4f);

    @Shadow
    public abstract void multiply(Quaternion quaternion);

    @Shadow
    public abstract Matrix4f copy();
    //#else
    //$$ @Shadow
    //$$ @Final
    //$$ private float[] values;
    //#endif

    @Override
    public void setIdentityCompat() {
    //#if MC > 11404
        this.setIdentity();
    //#else
    //$$     this.values[0] = 1.0F;
    //$$     this.values[4] = 0.0F;
    //$$     this.values[8] = 0.0F;
    //$$     this.values[12] = 0.0F;
    //$$     this.values[1] = 0.0F;
    //$$     this.values[5] = 1.0F;
    //$$     this.values[9] = 0.0F;
    //$$     this.values[13] = 0.0F;
    //$$     this.values[2] = 0.0F;
    //$$     this.values[6] = 0.0F;
    //$$     this.values[10] = 1.0F;
    //$$     this.values[14] = 0.0F;
    //$$     this.values[3] = 0.0F;
    //$$     this.values[7] = 0.0F;
    //$$     this.values[11] = 0.0F;
    //$$     this.values[15] = 1.0F;
    //#endif
    }

    @Override
    public void multiplyWithTranslationCompat(float f, float g, float h) {
    //#if MC > 11605
        this.multiplyWithTranslation(f, g, h);
    //#elseif MC > 11404
    //$$     this.multiply(Matrix4f.createTranslateMatrix(f, g, h));
    //#else
    //$$     this.values[12] += this.values[0] * f + this.values[4] * g + this.values[8] * h;
    //$$     this.values[13] += this.values[1] * f + this.values[5] * g + this.values[9] * h;
    //$$     this.values[14] += this.values[2] * f + this.values[6] * g + this.values[10] * h;
    //$$     this.values[15] += this.values[3] * f + this.values[7] * g + this.values[11] * h;
    //#endif
    }

    @Override
    public void multiplyCompat(Matrix4f matrix4f) {
        //#if MC > 11404
        this.multiply(matrix4f);
        //#else
        //$$ float[] matrix4fValues = ((AccessorMatrix4f) (Object) matrix4f).getValues();
        //$$ float f = this.values[0] * matrix4fValues[0] + this.values[4] * matrix4fValues[1] + this.values[8] * matrix4fValues[2] + this.values[12] * matrix4fValues[3];
        //$$ float g = this.values[0] * matrix4fValues[4] + this.values[4] * matrix4fValues[5] + this.values[8] * matrix4fValues[6] + this.values[12] * matrix4fValues[7];
        //$$ float h = this.values[0] * matrix4fValues[8] + this.values[4] * matrix4fValues[9] + this.values[8] * matrix4fValues[10] + this.values[12] * matrix4fValues[11];
        //$$ float i = this.values[0] * matrix4fValues[12] + this.values[4] * matrix4fValues[13] + this.values[8] * matrix4fValues[14] + this.values[12] * matrix4fValues[15];
        //$$ float j = this.values[1] * matrix4fValues[0] + this.values[5] * matrix4fValues[1] + this.values[9] * matrix4fValues[2] + this.values[13] * matrix4fValues[3];
        //$$ float k = this.values[1] * matrix4fValues[4] + this.values[5] * matrix4fValues[5] + this.values[9] * matrix4fValues[6] + this.values[13] * matrix4fValues[7];
        //$$ float l = this.values[1] * matrix4fValues[8] + this.values[5] * matrix4fValues[9] + this.values[9] * matrix4fValues[10] + this.values[13] * matrix4fValues[11];
        //$$ float m = this.values[1] * matrix4fValues[12] + this.values[5] * matrix4fValues[13] + this.values[9] * matrix4fValues[14] + this.values[13] * matrix4fValues[15];
        //$$ float n = this.values[2] * matrix4fValues[0] + this.values[6] * matrix4fValues[1] + this.values[10] * matrix4fValues[2] + this.values[14] * matrix4fValues[3];
        //$$ float o = this.values[2] * matrix4fValues[4] + this.values[6] * matrix4fValues[5] + this.values[10] * matrix4fValues[6] + this.values[14] * matrix4fValues[7];
        //$$ float p = this.values[2] * matrix4fValues[8] + this.values[6] * matrix4fValues[9] + this.values[10] * matrix4fValues[10] + this.values[14] * matrix4fValues[11];
        //$$ float q = this.values[2] * matrix4fValues[12] + this.values[6] * matrix4fValues[13] + this.values[10] * matrix4fValues[14] + this.values[14] * matrix4fValues[15];
        //$$ float r = this.values[3] * matrix4fValues[0] + this.values[7] * matrix4fValues[1] + this.values[11] * matrix4fValues[2] + this.values[15] * matrix4fValues[3];
        //$$ float s = this.values[3] * matrix4fValues[4] + this.values[7] * matrix4fValues[5] + this.values[11] * matrix4fValues[6] + this.values[15] * matrix4fValues[7];
        //$$ float t = this.values[3] * matrix4fValues[8] + this.values[7] * matrix4fValues[9] + this.values[11] * matrix4fValues[10] + this.values[15] * matrix4fValues[11];
        //$$ float u = this.values[3] * matrix4fValues[12] + this.values[7] * matrix4fValues[13] + this.values[11] * matrix4fValues[14] + this.values[15] * matrix4fValues[15];
        //$$ this.values[0] = f;
        //$$ this.values[4] = g;
        //$$ this.values[8] = h;
        //$$ this.values[12] = i;
        //$$ this.values[1] = j;
        //$$ this.values[5] = k;
        //$$ this.values[9] = l;
        //$$ this.values[13] = m;
        //$$ this.values[2] = n;
        //$$ this.values[6] = o;
        //$$ this.values[10] = p;
        //$$ this.values[14] = q;
        //$$ this.values[3] = r;
        //$$ this.values[7] = s;
        //$$ this.values[11] = t;
        //$$ this.values[15] = u;
        //#endif
    }

    public void multiplyCompat(Quaternion quaternion) {
        //#if MC > 11404
        this.multiply(quaternion);
        //#else
        //$$ this.multiplyCompat(new Matrix4f(quaternion));
        //#endif
    }

    public Matrix4f copyCompat() {
        //#if MC > 11404
        return this.copy();
        //#else
        //$$ Matrix4f ret = new Matrix4f();
        //$$
        //$$ for (int i = 0; i < 4; ++i) {
        //$$     for (int j = 0; j < 4; ++j) {
        //$$         ret.set(i, j, this.values[i + 4 * j]);
        //$$     }
        //$$ }
        //$$ return ret;
        //#endif
    }
}
