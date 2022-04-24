package top.hendrixshen.magiclib.test.compat.minecraft.blaze3d.vertex;

import com.mojang.blaze3d.vertex.VertexFormat;
import top.hendrixshen.magiclib.compat.minecraft.blaze3d.vertex.VertexFormatCompatApi;

public class TestVertexFormat {
    public static void test() {
        //#if MC > 11605
        VertexFormat.Mode mode = VertexFormatCompatApi.Mode.QUADS;
        //#else
        //$$ int mode = VertexFormatCompatApi.Mode.QUADS;
        //#endif
    }
}
