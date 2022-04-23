package top.hendrixshen.magiclib.compat.minecraft.blaze3d.vertex;

import com.mojang.blaze3d.vertex.VertexFormat;

//#if MC <= 11605
//$$ import org.lwjgl.opengl.GL11;
//#endif

public interface VertexFormatCompatApi {
    class Mode {
        //#if MC > 11605
        public static final VertexFormat.Mode QUADS = VertexFormat.Mode.QUADS;
        //#else
        //$$ public static final int QUADS = GL11.GL_QUADS;
        //#endif
    }
}