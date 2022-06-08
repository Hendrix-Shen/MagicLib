package top.hendrixshen.magiclib.compat.minecraft.client.renderer.entity;

import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Entity;

@Environment(EnvType.CLIENT)
public interface EntityRenderDispatcherCompatApi {
    default double distanceToSqrCompat(Entity entity) {
        throw new UnsupportedOperationException();
    }

    default Quaternion cameraOrientationCompat() {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11404
    //$$ default double distanceToSqr(Entity entity) {
    //$$     return this.distanceToSqrCompat(entity);
    //$$ }
    //$$
    //$$ default Quaternion cameraOrientation() {
    //$$     return this.cameraOrientationCompat();
    //$$ }
    //#endif
}