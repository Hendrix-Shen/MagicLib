package top.hendrixshen.magiclib.compat.minecraft.api.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Entity;
import com.mojang.math.Quaternion;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
public interface EntityRenderDispatcherCompatApi {
    default double distanceToSqrCompat(Entity entity) {
        throw new UnImplCompatApiException();
    }

    default Quaternion cameraOrientationCompat() {
        throw new UnImplCompatApiException();
    }

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