package top.hendrixshen.magiclib.compat.minecraft.api.client.gui;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.network.chat.Component;
import com.mojang.math.Matrix4f;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Environment(EnvType.CLIENT)
public interface FontCompatApi {
    default int widthCompat(Component component) {
        throw new UnImplCompatApiException();
    }

    default int drawInBatch(String text, float x, float y, int color, boolean shadow, Matrix4f matrix4f, boolean seeThrough, int backgroundColor, int light) {
        throw new UnImplCompatApiException();
    }

    default int drawInBatch(Component component, float x, float y, int color, boolean shadow, Matrix4f matrix4f, boolean seeThrough, int backgroundColor, int light) {
        throw new UnImplCompatApiException();
    }

    //#if MC < 11600
    //$$ default int width(Component component) {
    //$$     return this.widthCompat(component);
    //$$ }
    //#endif
}
