package top.hendrixshen.magiclib.mixin.compat.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.client.renderer.entity.EntityRenderDispatcherCompatApi;

//#if MC < 11500
//$$ import net.minecraft.client.Camera;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public abstract class MixinEntityRenderDispatcher implements EntityRenderDispatcherCompatApi {
    //#if MC > 11404
    @Shadow
    public abstract Quaternionf cameraOrientation();

    @Shadow
    public abstract double distanceToSqr(Entity entity);
    //#else
    //$$ @Shadow
    //$$ public Camera camera;
    //#endif

    @Override
    public double distanceToSqrCompat(Entity entity) {
        //#if MC > 11404
        return this.distanceToSqr(entity);
        //#else
        //$$ return this.camera.getPosition().distanceToSqr(entity.position());
        //#endif
    }

    @Override
    public Quaternionf cameraOrientationCompat() {
        //#if MC > 11404
        return this.cameraOrientation();
        //#else
        //$$ return camera.rotation();
        //#endif
    }
}