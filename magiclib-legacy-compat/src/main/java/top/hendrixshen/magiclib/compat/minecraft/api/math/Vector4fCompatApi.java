package top.hendrixshen.magiclib.compat.minecraft.api.math;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

public interface Vector4fCompatApi {
    default void transformCompat(Matrix4f matrix4f) {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11404
    //$$ default void transform(Matrix4f matrix4f) {
    //$$     this.transformCompat(matrix4f);
    //$$ }
    //#endif
}
