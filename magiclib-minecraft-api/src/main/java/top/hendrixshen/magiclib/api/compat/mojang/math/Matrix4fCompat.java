package top.hendrixshen.magiclib.api.compat.mojang.math;

import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11902
//$$ import org.joml.Matrix4f;
//$$ import org.joml.Quaternionf;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 11903
import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.compat.mojang.math.Matrix4fCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface Matrix4fCompat extends Provider<Matrix4f> {
    static @NotNull Matrix4fCompat of(@NotNull Matrix4f matrix4f) {
        return new Matrix4fCompatImpl(matrix4f);
    }

    static @NotNull Matrix4fCompat createScaleMatrix(float x, float y, float z) {
        //#if MC > 11902
        //$$ return Matrix4fCompat.of(new Matrix4f().scale(x, y, z));
        //#elseif MC > 11404
        return Matrix4fCompat.of(Matrix4f.createScaleMatrix(x, y, z));
        //#else
        //$$ Matrix4f matrix4f = new Matrix4f();
        //$$ matrix4f.set(0, 0, x);
        //$$ matrix4f.set(1, 1, y);
        //$$ matrix4f.set(2, 2, z);
        //$$ matrix4f.set(3, 3, 1);
        //$$ return Matrix4fCompat.of(matrix4f);
        //#endif
    }

    static @NotNull Matrix4fCompat createTranslateMatrix(float x, float y, float z) {
        //#if MC > 11902
        //$$ return Matrix4fCompat.of(new Matrix4f().translation(x, y, z));
        //#elseif MC > 11404
        return Matrix4fCompat.of(Matrix4f.createTranslateMatrix(x, y, z));
        //#else
        //$$ Matrix4f matrix4f = new Matrix4f();
        //$$ matrix4f.set(0, 0, 1.0F);
        //$$ matrix4f.set(1, 1, 1.0F);
        //$$ matrix4f.set(2, 2, 1.0F);
        //$$ matrix4f.set(3, 3, 1.0F);
        //$$ matrix4f.set(0, 3, x);
        //$$ matrix4f.set(1, 3, y);
        //$$ matrix4f.set(2, 3, z);
        //$$ return Matrix4fCompat.of(matrix4f);
        //#endif
    }

    void setIdentity();

    void multiplyWithTranslation(float x, float y, float z);

    void multiply(Matrix4f matrix4f);

    void multiply(Quaternion quaternion);

    Matrix4fCompat copy();
}
