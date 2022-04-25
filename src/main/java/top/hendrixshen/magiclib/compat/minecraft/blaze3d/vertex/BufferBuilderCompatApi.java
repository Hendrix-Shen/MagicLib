package top.hendrixshen.magiclib.compat.minecraft.blaze3d.vertex;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.math.Matrix4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;


@Environment(EnvType.CLIENT)
public interface BufferBuilderCompatApi {
    default BufferBuilder vertexCompat(Matrix4f matrix4f, float x, float y, float z) {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11404
    //$$ default BufferBuilder vertex(Matrix4f matrix4f, float x, float y, float z) {
    //$$     return this.vertexCompat(matrix4f, x, y, z);
    //$$ }
    //#endif
}
