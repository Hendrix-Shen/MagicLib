package top.hendrixshen.magiclib.impl.compat.mojang.math;

import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.mojang.math.Vector4fCompat;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.Vector4fAccessor;

@Environment(EnvType.CLIENT)
public class Vector4fCompatImpl extends AbstractCompat<Vector4f> implements Vector4fCompat {
    public Vector4fCompatImpl(@NotNull Vector4f type) {
        super(type);
    }

    @Override
    public void transform(@NotNull Matrix4f matrix4f) {
        //#if MC > 11902
        //$$ matrix4f.transform(this.get());
        //#elseif MC > 11404
        //$$ this.get().transform(matrix4f);
        //#else
        float[] v = ((Vector4fAccessor) this.get()).magiclib$getValues();
        float x = v[0];
        float y = v[1];
        float z = v[2];
        float w = v[3];
        Matrix4fCompatImpl m = new Matrix4fCompatImpl(matrix4f);
        v[0] = m.get(0, 0) * x + m.get(0, 1) * y + m.get(0, 2) * z + m.get(0, 3) * w;
        v[1] = m.get(1, 0) * x + m.get(1, 1) * y + m.get(1, 2) * z + m.get(1, 3) * w;
        v[2] = m.get(2, 0) * x + m.get(2, 1) * y + m.get(2, 2) * z + m.get(2, 3) * w;
        v[3] = m.get(3, 0) * x + m.get(3, 1) * y + m.get(3, 2) * z + m.get(3, 3) * w;
        //#endif
    }
}
