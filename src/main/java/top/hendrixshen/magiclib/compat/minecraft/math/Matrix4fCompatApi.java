package top.hendrixshen.magiclib.compat.minecraft.math;

import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface Matrix4fCompatApi {
    static Matrix4f createScaleMatrix(float f, float g, float h) {
        //#if MC > 11404
        return Matrix4f.createScaleMatrix(f, g, h);
        //#else
        //$$ Matrix4f matrix4f = new Matrix4f();
        //$$ matrix4f.set(0, 0, f);
        //$$ matrix4f.set(1, 1, g);
        //$$ matrix4f.set(2, 2, h);
        //$$ matrix4f.set(3, 3, 1);
        //$$ return matrix4f;
        //#endif
    }

    default void setIdentityCompat() {
        throw new UnsupportedOperationException();
    }

    default void multiplyWithTranslationCompat(float f, float g, float h) {
        throw new UnsupportedOperationException();
    }

    default void multiplyCompat(Matrix4f matrix4f) {
        throw new UnsupportedOperationException();
    }

    default void multiplyCompat(Quaternion quaternion) {
        throw new UnsupportedOperationException();
    }

    default Matrix4f copyCompat() {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11605
    //$$ default void multiplyWithTranslation(float f, float g, float h) {
    //$$     this.multiplyWithTranslationCompat(f, g, h);
    //$$ }
    //#endif

    //#if MC <= 11404
    //$$ default void setIdentity() {
    //$$     this.setIdentityCompat();
    //$$ }
    //$$
    //$$ default void multiply(Matrix4f matrix4f) {
    //$$     this.multiplyCompat(matrix4f);
    //$$ }
    //$$
    //$$ default void multiply(Quaternion quaternion) {
    //$$     this.multiplyCompat(quaternion);
    //$$ }
    //$$
    //$$ default Matrix4f copy() {
    //$$     return this.copyCompat();
    //$$ }
    //#endif

}
