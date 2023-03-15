package top.hendrixshen.magiclib.compat.minecraft.api.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Quaternionf;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Environment(EnvType.CLIENT)
public interface CameraCompatApi {
    default Quaternionf rotationCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11404
    //$$ default Quaternion rotation() {
    //$$     return this.rotationCompat();
    //$$ }
    //#endif
}
