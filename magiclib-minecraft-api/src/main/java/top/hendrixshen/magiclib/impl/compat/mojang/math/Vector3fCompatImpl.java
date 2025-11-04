package top.hendrixshen.magiclib.impl.compat.mojang.math;

import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11902
//$$ import org.joml.Quaternionf;
//$$ import org.joml.Vector3f;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 11903
import com.mojang.math.Vector3f;
//#endif

//#if MC < 11500
//$$ import com.mojang.math.Quaternion;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.mojang.math.QuaternionCompat;
import top.hendrixshen.magiclib.api.compat.mojang.math.Vector3fCompat;

public class Vector3fCompatImpl extends AbstractCompat<Vector3f> implements Vector3fCompat {
    public Vector3fCompatImpl(@NotNull Vector3f type) {
        super(type);
    }

    @Override
    public QuaternionCompat rotationDegrees(float degrees) {
        return QuaternionCompat.of(
                //#if MC > 11902
                //$$ new Quaternionf().rotationAxis(degrees, this.get())
                //#elseif MC > 11404
                this.get().rotationDegrees(degrees)
                //#else
                //$$ new Quaternion(this.get(), degrees, true)
                //#endif
        );
    }
}
