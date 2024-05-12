package top.hendrixshen.magiclib.compat.minecraft.api.math;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.mojang.math.Matrix4f;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
public interface Vector4fCompatApi {
    default void transformCompat(Matrix4f matrix4f) {
        throw new UnImplCompatApiException();
    }

    //#if MC < 11500
    //$$ default void transform(Matrix4f matrix4f) {
    //$$     this.transformCompat(matrix4f);
    //$$ }
    //#endif
}
