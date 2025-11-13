package top.hendrixshen.magiclib.api.compat.mojang.math;

import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11902
//$$ import org.joml.Quaternionf;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 11903
import com.mojang.math.Quaternion;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.compat.mojang.math.QuaternionCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface QuaternionCompat extends Provider<Quaternion> {
    QuaternionCompat ONE = QuaternionCompat.of(new Quaternion(0.0F, 0.0F, 0.0F, 1.0F));

    static @NotNull QuaternionCompat of(@NotNull Quaternion quaternion) {
        return new QuaternionCompatImpl(quaternion);
    }

    void mul(float f);
}
