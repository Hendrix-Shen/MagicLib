package top.hendrixshen.magiclib.api.compat.mojang.math;

import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11902
//$$ import org.joml.Vector3f;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 11903
import com.mojang.math.Vector3f;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import top.hendrixshen.magiclib.impl.compat.mojang.math.Vector3fCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

@Environment(EnvType.CLIENT)
public interface Vector3fCompat extends Provider<Vector3f> {
    static @NotNull Vector3fCompat of(@NotNull Vector3f vector3f) {
        return new Vector3fCompatImpl(vector3f);
    }

    QuaternionCompat rotationDegrees(float degrees);
}
