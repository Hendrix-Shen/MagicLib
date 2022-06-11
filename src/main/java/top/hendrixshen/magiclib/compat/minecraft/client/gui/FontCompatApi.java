package top.hendrixshen.magiclib.compat.minecraft.client.gui;

import com.mojang.math.Matrix4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public interface FontCompatApi {
    // TODO add test
    default int widthCompat(Component component) {
        throw new UnsupportedOperationException();
    }

    default int drawInBatch(String text, float x, float y, int color, boolean shadow, Matrix4f matrix4f,
                            boolean seeThrough, int backgroundColor, int light) {
        throw new UnsupportedOperationException();
    }

    default int drawInBatch(Component component, float x, float y, int color, boolean shadow, Matrix4f matrix4f,
                            boolean seeThrough, int backgroundColor, int light) {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11502
    //$$ default int width(Component component) {
    //$$     return this.widthCompat(component);
    //$$ }
    //#endif
}
