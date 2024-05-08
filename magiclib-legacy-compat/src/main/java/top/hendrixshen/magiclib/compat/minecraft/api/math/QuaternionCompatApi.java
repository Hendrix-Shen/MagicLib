package top.hendrixshen.magiclib.compat.minecraft.api.math;

import com.mojang.math.Quaternion;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

public interface QuaternionCompatApi {
    Quaternion ONE = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);

    default void mulCompat(float f) {
        throw new UnImplCompatApiException();
    }

    default void normalizeCompat() {
        throw new UnImplCompatApiException();
    }

    default Quaternion copyCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11404
    //$$ default void mul(float f) {
    //$$      this.mulCompat(f);
    //$$ }
    //$$
    //$$ default void normalize() {
    //$$     this.normalizeCompat();
    //$$ }
    //$$
    //$$ default Quaternion copy() {
    //$$     return this.copyCompat();
    //$$ }
    //#endif
}
