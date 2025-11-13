package top.hendrixshen.magiclib.compat.minecraft.api.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

//#if MC < 12109
import com.mojang.math.Quaternion;
//#endif

@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
public interface EntityRenderDispatcherCompatApi {
    default double distanceToSqrCompat(Entity entity) {
        throw new UnImplCompatApiException();
    }

    //#if MC < 12109
    default Quaternion cameraOrientationCompat() {
        throw new UnImplCompatApiException();
    }
    //#endif

    //#if MC < 11500
    //$$ default double distanceToSqr(Entity entity) {
    //$$     return this.distanceToSqrCompat(entity);
    //$$ }
    //$$
    //$$ default Quaternion cameraOrientation() {
    //$$     return this.cameraOrientationCompat();
    //$$ }
    //#endif
}
