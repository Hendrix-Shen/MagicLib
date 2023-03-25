package top.hendrixshen.magiclib.compat.minecraft.api.math;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Quaternionf;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

public interface QuaternionCompatApi {
    Quaternionf ONE = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);

    default void mulCompat(float f) {
        throw new UnImplCompatApiException();
    }

    default void normalizeCompat() {
        throw new UnImplCompatApiException();
    }

    default Quaternionf copyCompat() {
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
