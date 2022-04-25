package top.hendrixshen.magiclib.compat.minecraft.math;

import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface QuaternionCompatApi {

    //#if MC > 11404
    Quaternion ONE = Quaternion.ONE;
    //#else
    //$$ Quaternion ONE = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
    //#endif

    default void mulCompat(float f) {
        throw new UnsupportedOperationException();
    }

    default void normalizeCompat() {
        throw new UnsupportedOperationException();
    }

    default Quaternion copyCompat() {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11404
    //$$ default void mul(float f) {
    //$$      this.mulCompat(f);
    //$$ }

    //$$ default void normalize() {
    //$$     this.normalizeCompat();
    //$$ }

    //$$ default Quaternion copy() {
    //$$     return this.copyCompat();
    //$$ }
    //#endif
}
