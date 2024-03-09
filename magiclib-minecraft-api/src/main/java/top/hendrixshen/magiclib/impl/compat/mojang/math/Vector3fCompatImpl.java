package top.hendrixshen.magiclib.impl.compat.mojang.math;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.mojang.math.QuaternionCompat;
import top.hendrixshen.magiclib.api.compat.mojang.math.Vector3fCompat;

@Environment(EnvType.CLIENT)
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
                //$$ this.get().rotationDegrees(degrees)
                //#else
                new Quaternion(this.get(), degrees, true)
                //#endif
        );
    }
}
