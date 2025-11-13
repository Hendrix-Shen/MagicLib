package top.hendrixshen.magiclib.api.compat.mojang.math;

import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11902
//$$ import org.joml.Matrix4f;
//$$ import org.joml.Vector4f;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 11903
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.compat.mojang.math.Vector4fCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface Vector4fCompat extends Provider<Vector4f> {
    static @NotNull Vector4fCompat of(@NotNull Vector4f vector4f) {
        return new Vector4fCompatImpl(vector4f);
    }

    void transform(@NotNull Matrix4f matrix4f);
}
