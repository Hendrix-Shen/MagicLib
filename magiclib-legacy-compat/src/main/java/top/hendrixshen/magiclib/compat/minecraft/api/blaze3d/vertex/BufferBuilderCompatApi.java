package top.hendrixshen.magiclib.compat.minecraft.api.blaze3d.vertex;

import com.mojang.blaze3d.vertex.BufferBuilder;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.mojang.math.Matrix4f;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
public interface BufferBuilderCompatApi {
    default BufferBuilder vertexCompat(Matrix4f matrix4f, float x, float y, float z) {
        throw new UnImplCompatApiException();
    }

    //#if MC < 11500
    //$$ default BufferBuilder vertex(Matrix4f matrix4f, float x, float y, float z) {
    //$$     return this.vertexCompat(matrix4f, x, y, z);
    //$$ }
    //#endif
}
