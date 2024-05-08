package top.hendrixshen.magiclib.compat.minecraft.api.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.mojang.math.Quaternion;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Environment(EnvType.CLIENT)
public interface CameraCompatApi {
    default Quaternion rotationCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11404
    //$$ default Quaternion rotation() {
    //$$     return this.rotationCompat();
    //$$ }
    //#endif
}
