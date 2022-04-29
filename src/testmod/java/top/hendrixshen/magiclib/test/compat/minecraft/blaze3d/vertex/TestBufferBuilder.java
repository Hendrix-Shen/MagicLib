package top.hendrixshen.magiclib.test.compat.minecraft.blaze3d.vertex;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.math.Matrix4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import top.hendrixshen.magiclib.compat.minecraft.blaze3d.vertex.VertexFormatCompatApi;

@Environment(EnvType.CLIENT)
public class TestBufferBuilder {

    public static void test() {
        BufferBuilder bufferBuilder = new BufferBuilder(32);
        bufferBuilder.begin(VertexFormatCompatApi.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertexCompat(new Matrix4f(), 0, 0, 0);
        // TODO
        //#if MC > 11605
        bufferBuilder.vertex(new Matrix4f(), 0, 0, 0);
        //#endif
    }
}
