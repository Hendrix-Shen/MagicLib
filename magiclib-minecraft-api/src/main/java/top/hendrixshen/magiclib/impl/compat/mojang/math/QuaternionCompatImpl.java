package top.hendrixshen.magiclib.impl.compat.mojang.math;

import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.mojang.math.QuaternionCompat;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.QuaternionAccessor;
import top.hendrixshen.magiclib.util.MiscUtil;

@Environment(EnvType.CLIENT)
public class QuaternionCompatImpl extends AbstractCompat<Quaternion> implements QuaternionCompat {
    public QuaternionCompatImpl(@NotNull Quaternion type) {
        super(type);
    }

    @Override
    public void mul(float f) {
        //#if MC > 11404
        this.get().mul(f);
        //#else
        //$$ float[] v = ((QuaternionAccessor) MiscUtil.cast(this.get())).magiclib$getValues();
        //$$ v[0] *= f;
        //$$ v[1] *= f;
        //$$ v[2] *= f;
        //$$ v[3] *= f;
        //#endif
    }

    public void normalize() {
        //#if MC > 11404
        this.get().normalize();
        //#else
        //$$ Quaternion quaternion = this.get();
        //$$ float k = quaternion.i() * quaternion.i() + quaternion.j() * quaternion.j() +
        //$$         quaternion.k() * quaternion.k() + quaternion.r() * quaternion.r();
        //$$
        //$$ if (k > 1.0E-6F) {
        //$$     this.mul((float) Mth.fastInvSqrt(k));
        //$$ } else {
        //$$     this.mul(0.0F);
        //$$ }
        //#endif
    }

    public QuaternionCompat copy() {
        return QuaternionCompat.of(
                //#if MC > 11902
                //$$ new Quaternionf(this.get())
                //#elseif MC > 11404
                this.get().copy()
                //#else
                //$$ new Quaternion(this.get())
                //#endif
        );
    }
}
