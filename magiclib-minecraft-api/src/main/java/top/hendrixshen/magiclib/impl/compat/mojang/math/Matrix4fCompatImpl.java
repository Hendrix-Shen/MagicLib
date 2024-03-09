package top.hendrixshen.magiclib.impl.compat.mojang.math;

import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.mojang.math.Matrix4fCompat;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.Matrix4fAccessor;
import top.hendrixshen.magiclib.util.MiscUtil;

@Environment(EnvType.CLIENT)
public class Matrix4fCompatImpl extends AbstractCompat<Matrix4f> implements Matrix4fCompat {
    public Matrix4fCompatImpl(@NotNull Matrix4f type) {
        super(type);
    }

    @Override
    public void setIdentity() {
        //#if MC > 11902
        //$$ this.get().identity();
        //#elseif MC > 11404
        //$$ this.get().setIdentity();
        //#else
        Matrix4f matrix4f = this.get();
        matrix4f.set(0, 0, 1.0F);
        matrix4f.set(0, 1, 0.0F);
        matrix4f.set(0, 2, 0.0F);
        matrix4f.set(0, 3, 0.0F);
        matrix4f.set(1, 0, 0.0F);
        matrix4f.set(1, 1, 1.0F);
        matrix4f.set(1, 2, 0.0F);
        matrix4f.set(1, 3, 0.0F);
        matrix4f.set(2, 0, 0.0F);
        matrix4f.set(2, 1, 0.0F);
        matrix4f.set(2, 2, 1.0F);
        matrix4f.set(2, 3, 0.0F);
        matrix4f.set(3, 0, 0.0F);
        matrix4f.set(3, 1, 0.0F);
        matrix4f.set(3, 2, 0.0F);
        matrix4f.set(3, 3, 1.0F);
        //#endif
    }

    @Override
    public void multiplyWithTranslation(float f, float g, float h) {
        this.multiply(Matrix4fCompat.createTranslateMatrix(f, g, h).get());
    }

    @Override
    public void multiply(Matrix4f matrix4f) {
        //#if MC > 11902
        //$$ this.get().mul(matrix4f);
        //#elseif MC > 11404
        //$$ this.get().multiply(matrix4f);
        //#else
        Matrix4fCompatImpl m = new Matrix4fCompatImpl(matrix4f);
        float m00 = this.get(0, 0) * m.get(0, 0) + this.get(0, 1) * m.get(1, 0) + this.get(0, 2) * m.get(2, 0) + this.get(0, 3) * m.get(3, 0);
        float m01 = this.get(0, 0) * m.get(0, 1) + this.get(0, 1) * m.get(1, 1) + this.get(0, 2) * m.get(2, 1) + this.get(0, 3) * m.get(3, 1);
        float m02 = this.get(0, 0) * m.get(0, 2) + this.get(0, 1) * m.get(1, 2) + this.get(0, 2) * m.get(2, 2) + this.get(0, 3) * m.get(3, 2);
        float m03 = this.get(0, 0) * m.get(0, 3) + this.get(0, 1) * m.get(1, 3) + this.get(0, 2) * m.get(2, 3) + this.get(0, 3) * m.get(3, 3);
        float m10 = this.get(1, 0) * m.get(0, 0) + this.get(1, 1) * m.get(1, 0) + this.get(1, 2) * m.get(2, 0) + this.get(1, 3) * m.get(3, 0);
        float m11 = this.get(1, 0) * m.get(0, 1) + this.get(1, 1) * m.get(1, 1) + this.get(1, 2) * m.get(2, 1) + this.get(1, 3) * m.get(3, 1);
        float m12 = this.get(1, 0) * m.get(0, 2) + this.get(1, 1) * m.get(1, 2) + this.get(1, 2) * m.get(2, 2) + this.get(1, 3) * m.get(3, 2);
        float m13 = this.get(1, 0) * m.get(0, 3) + this.get(1, 1) * m.get(1, 3) + this.get(1, 2) * m.get(2, 3) + this.get(1, 3) * m.get(3, 3);
        float m20 = this.get(2, 0) * m.get(0, 0) + this.get(2, 1) * m.get(1, 0) + this.get(2, 2) * m.get(2, 0) + this.get(2, 3) * m.get(3, 0);
        float m21 = this.get(2, 0) * m.get(0, 1) + this.get(2, 1) * m.get(1, 1) + this.get(2, 2) * m.get(2, 1) + this.get(2, 3) * m.get(3, 1);
        float m22 = this.get(2, 0) * m.get(0, 2) + this.get(2, 1) * m.get(1, 2) + this.get(2, 2) * m.get(2, 2) + this.get(2, 3) * m.get(3, 2);
        float m23 = this.get(2, 0) * m.get(0, 3) + this.get(2, 1) * m.get(1, 3) + this.get(2, 2) * m.get(2, 3) + this.get(2, 3) * m.get(3, 3);
        float m30 = this.get(3, 0) * m.get(0, 0) + this.get(3, 1) * m.get(1, 0) + this.get(3, 2) * m.get(2, 0) + this.get(3, 3) * m.get(3, 0);
        float m31 = this.get(3, 0) * m.get(0, 1) + this.get(3, 1) * m.get(1, 1) + this.get(3, 2) * m.get(2, 1) + this.get(3, 3) * m.get(3, 1);
        float m32 = this.get(3, 0) * m.get(0, 2) + this.get(3, 1) * m.get(1, 2) + this.get(3, 2) * m.get(2, 2) + this.get(3, 3) * m.get(3, 2);
        float m33 = this.get(3, 0) * m.get(0, 3) + this.get(3, 1) * m.get(1, 3) + this.get(3, 2) * m.get(2, 3) + this.get(3, 3) * m.get(3, 3);
        this.set(0, 0, m00);
        this.set(0, 1, m01);
        this.set(0, 2, m02);
        this.set(0, 3, m03);
        this.set(1, 0, m10);
        this.set(1, 1, m11);
        this.set(1, 2, m12);
        this.set(1, 3, m13);
        this.set(2, 0, m20);
        this.set(2, 1, m21);
        this.set(2, 2, m22);
        this.set(2, 3, m23);
        this.set(3, 0, m30);
        this.set(3, 1, m31);
        this.set(3, 2, m32);
        this.set(3, 3, m33);
        //#endif
    }

    @Override
    public void multiply(Quaternion quaternion) {
        this.multiply(
                //#if MC > 11902
                //$$ new Matrix4f().rotation(quaternion)
                //#else
                new Matrix4f(quaternion)
                //#endif
        );
    }

    @Override
    public Matrix4fCompat copy() {
        //#if MC > 11902
        //$$ return Matrix4fCompat.of(new Matrix4f(this.get()));
        //#elseif MC > 11404
        //$$ return Matrix4fCompat.of(this.get().copy());
        //#else
        Matrix4fCompatImpl matrix4f = new Matrix4fCompatImpl(new Matrix4f());
        matrix4f.set(0, 0, this.get(0, 0));
        matrix4f.set(0, 1, this.get(0, 1));
        matrix4f.set(0, 2, this.get(0, 2));
        matrix4f.set(0, 3, this.get(0, 3));
        matrix4f.set(1, 0, this.get(1, 0));
        matrix4f.set(1, 1, this.get(1, 1));
        matrix4f.set(1, 2, this.get(1, 2));
        matrix4f.set(1, 3, this.get(1, 3));
        matrix4f.set(2, 0, this.get(2, 0));
        matrix4f.set(2, 1, this.get(2, 1));
        matrix4f.set(2, 2, this.get(2, 2));
        matrix4f.set(2, 3, this.get(2, 3));
        matrix4f.set(3, 0, this.get(3, 0));
        matrix4f.set(3, 1, this.get(3, 1));
        matrix4f.set(3, 2, this.get(3, 2));
        matrix4f.set(3, 3, this.get(3, 3));
        return matrix4f;
        //#endif
    }

    //#if MC < 11500
    private float get(int l, int c) {
        return ((Matrix4fAccessor) MiscUtil.cast(this.get())).magiclib$getValues()[l + 4 * c];
    }

    public void set(int l, int c, float value) {
        this.get().set(l, c, value);
    }
    //#endif
}
