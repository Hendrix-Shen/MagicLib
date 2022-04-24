package top.hendrixshen.magiclib.compat.minecraft.client.gui;

//#if MC <= 11502
//$$ import net.minecraft.client.renderer.MultiBufferSource;
//#endif


import com.mojang.math.Matrix4f;
import net.minecraft.network.chat.Component;

public interface FontCompatApi {
    //#if MC <= 11502
    //$$ default int width(Component component) {
    //$$     throw new UnsupportedOperationException();
    //$$ }
    //$$ default int drawInBatch(Component component, float x, float y, int color, boolean shadow, Matrix4f matrix4f,
    //$$                         MultiBufferSource multiBufferSource, boolean seeThrough, int backgroundColor, int light) {
    //$$     throw new UnsupportedOperationException();
    //$$ }
    //#endif

    // TODO add compat

    default int drawInBatch(String text, float x, float y, int color, boolean shadow, Matrix4f matrix4f,
                            boolean seeThrough, int backgroundColor, int light) {
        throw new UnsupportedOperationException();
    }

    default int drawInBatch(Component component, float x, float y, int color, boolean shadow, Matrix4f matrix4f,
                            boolean seeThrough, int backgroundColor, int light) {
        throw new UnsupportedOperationException();
    }
}
