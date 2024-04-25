package top.hendrixshen.magiclib.api.compat.mojang.blaze3d.vertex;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

//#if MC > 11605
import com.mojang.blaze3d.vertex.VertexFormat;
//#else
//$$ import org.lwjgl.opengl.GL11;
//#endif

@Environment(EnvType.CLIENT)
public interface VertexFormatCompat {
    @Environment(EnvType.CLIENT)
    final class Mode {
        private Mode() {
            throw new AssertionError("No " + Mode.class.getName() + "instances for you!");
        }

        //#if MC > 11605
        public static VertexFormat.Mode LINES = VertexFormat.Mode.LINES;
        public static VertexFormat.Mode LINE_STRIP = VertexFormat.Mode.LINE_STRIP;
        public static VertexFormat.Mode DEBUG_LINES = VertexFormat.Mode.DEBUG_LINES;
        public static VertexFormat.Mode DEBUG_LINE_STRIP = VertexFormat.Mode.DEBUG_LINE_STRIP;
        public static VertexFormat.Mode TRIANGLES = VertexFormat.Mode.TRIANGLES;
        public static VertexFormat.Mode TRIANGLE_STRIP = VertexFormat.Mode.TRIANGLE_STRIP;
        public static VertexFormat.Mode TRIANGLE_FAN = VertexFormat.Mode.TRIANGLE_FAN;
        public static VertexFormat.Mode QUADS = VertexFormat.Mode.QUADS;
        //#else
        //$$ public static int LINES = GL11.GL_LINE_BIT;
        //$$ public static int LINE_STRIP = GL11.GL_TRIANGLE_STRIP;
        //$$ public static int DEBUG_LINES = GL11.GL_LINES;
        //$$ public static int DEBUG_LINE_STRIP = GL11.GL_LINE_STRIP;
        //$$ public static int TRIANGLES = GL11.GL_TRIANGLES;
        //$$ public static int TRIANGLE_STRIP = GL11.GL_TRIANGLE_STRIP;
        //$$ public static int TRIANGLE_FAN = GL11.GL_TRIANGLE_FAN;
        //$$ public static int QUADS = GL11.GL_QUADS;
        //#endif
    }
}
