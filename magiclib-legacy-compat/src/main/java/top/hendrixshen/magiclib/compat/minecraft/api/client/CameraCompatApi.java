package top.hendrixshen.magiclib.compat.minecraft.api.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import com.mojang.math.Quaternion;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
public interface CameraCompatApi {
    default Quaternion rotationCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC < 11500
    //$$ default Quaternion rotation() {
    //$$     return this.rotationCompat();
    //$$ }
    //#endif
}
