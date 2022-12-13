package top.hendrixshen.magiclib.compat.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public interface CameraCompatApi {
    default Quaternionf rotationCompat() {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11404
    //$$ default Quaternion rotation() {
    //$$     return this.rotationCompat();
    //$$ }
    //#endif
}
