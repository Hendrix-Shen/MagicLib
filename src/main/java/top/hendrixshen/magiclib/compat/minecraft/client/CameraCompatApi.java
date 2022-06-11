package top.hendrixshen.magiclib.compat.minecraft.client;

import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public interface CameraCompatApi {
    default Quaternion rotationCompat() {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11404
    //$$ default Quaternion rotation() {
    //$$     return this.rotationCompat();
    //$$ }
    //#endif
}
