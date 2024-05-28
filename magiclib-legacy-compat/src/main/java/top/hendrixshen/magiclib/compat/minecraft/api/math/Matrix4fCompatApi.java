package top.hendrixshen.magiclib.compat.minecraft.api.math;

import com.mojang.math.Matrix4f;
import com.mojang.math.Quaternion;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
public interface Matrix4fCompatApi {
    //#if MC > 11902
    //$$ /**
    //$$  * Deprecated api. Use {@link Matrix4f#scale(float, float, float)} instead.
    //$$  */
    //#endif
    static Matrix4f createScaleMatrix(float f, float g, float h) {
        //#if MC > 11902
        //$$ throw new UnsupportedOperationException("Don't use this method, use Matrix4f::scale instead");
        //#elseif MC > 11404
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
        throw new UnImplCompatApiException();
    }

    default void multiplyWithTranslationCompat(float f, float g, float h) {
        throw new UnImplCompatApiException();
    }

    default void multiplyCompat(Matrix4f matrix4f) {
        throw new UnImplCompatApiException();
    }

    default void multiplyCompat(Quaternion quaternion) {
        throw new UnImplCompatApiException();
    }

    default Matrix4f copyCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC < 11700
    default void multiplyWithTranslation(float f, float g, float h) {
        this.multiplyWithTranslationCompat(f, g, h);
    }
    //#endif

    //#if MC < 11500
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
