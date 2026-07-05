package top.hendrixshen.magiclib.api.compat.mojang.blaze3d.vertex;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 11700
import org.lwjgl.opengl.GL11;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 26.2
//$$ import com.mojang.blaze3d.PrimitiveTopology;
//#elseif MC > 11605
//$$ import com.mojang.blaze3d.vertex.VertexFormat;
//#endif
// CHECKSTYLE.ON: ImportOrder

public interface VertexFormatCompat {
    final class Mode {
        private Mode() {
            throw new AssertionError("No " + Mode.class.getName() + "instances for you!");
        }

        //#if MC >= 26.2
        //$$ public static PrimitiveTopology LINES = PrimitiveTopology.LINES;
        //$$ public static PrimitiveTopology DEBUG_LINES = PrimitiveTopology.DEBUG_LINES;
        //$$ public static PrimitiveTopology DEBUG_LINE_STRIP = PrimitiveTopology.DEBUG_LINE_STRIP;
        //$$ public static PrimitiveTopology TRIANGLES = PrimitiveTopology.TRIANGLES;
        //$$ public static PrimitiveTopology TRIANGLE_STRIP = PrimitiveTopology.TRIANGLE_STRIP;
        //$$ public static PrimitiveTopology TRIANGLE_FAN = PrimitiveTopology.TRIANGLE_FAN;
        //$$ public static PrimitiveTopology QUADS = PrimitiveTopology.QUADS;
        //#elseif MC > 11605
        //$$ public static VertexFormat.Mode LINES = VertexFormat.Mode.LINES;
        //#if MC < 12100
        //$$ public static VertexFormat.Mode LINE_STRIP = VertexFormat.Mode.LINE_STRIP;
        //#endif
        //$$ public static VertexFormat.Mode DEBUG_LINES = VertexFormat.Mode.DEBUG_LINES;
        //$$ public static VertexFormat.Mode DEBUG_LINE_STRIP = VertexFormat.Mode.DEBUG_LINE_STRIP;
        //$$ public static VertexFormat.Mode TRIANGLES = VertexFormat.Mode.TRIANGLES;
        //$$ public static VertexFormat.Mode TRIANGLE_STRIP = VertexFormat.Mode.TRIANGLE_STRIP;
        //$$ public static VertexFormat.Mode TRIANGLE_FAN = VertexFormat.Mode.TRIANGLE_FAN;
        //$$ public static VertexFormat.Mode QUADS = VertexFormat.Mode.QUADS;
        //#else
        public static int LINES = GL11.GL_LINE_BIT;
        public static int LINE_STRIP = GL11.GL_TRIANGLE_STRIP;
        public static int DEBUG_LINES = GL11.GL_LINES;
        public static int DEBUG_LINE_STRIP = GL11.GL_LINE_STRIP;
        public static int TRIANGLES = GL11.GL_TRIANGLES;
        public static int TRIANGLE_STRIP = GL11.GL_TRIANGLE_STRIP;
        public static int TRIANGLE_FAN = GL11.GL_TRIANGLE_FAN;
        public static int QUADS = GL11.GL_QUADS;
        //#endif
    }
}
