package top.hendrixshen.magiclib.compat.minecraft.math;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public interface QuaternionCompatApi {
    Quaternionf ONE = new Quaternionf(0.0F, 0.0F, 0.0F, 1.0F);

    default void mulCompat(float f) {
        throw new UnsupportedOperationException();
    }

    default void normalizeCompat() {
        throw new UnsupportedOperationException();
    }

    default Quaternionf copyCompat() {
        throw new UnsupportedOperationException();
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
