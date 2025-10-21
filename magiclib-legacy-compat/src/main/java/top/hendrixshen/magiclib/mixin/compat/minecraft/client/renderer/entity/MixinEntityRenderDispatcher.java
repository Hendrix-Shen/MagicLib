package top.hendrixshen.magiclib.mixin.compat.minecraft.client.renderer.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.client.renderer.entity.EntityRenderDispatcherCompatApi;

//#if MC < 12109
import com.mojang.math.Quaternion;
//#endif

//#if MC < 11500
//$$ import net.minecraft.client.Camera;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderDispatcher.class)
public abstract class MixinEntityRenderDispatcher implements EntityRenderDispatcherCompatApi {
    //#if MC > 11404
    //#if MC < 12109
    @Shadow
    public abstract Quaternion cameraOrientation();
    //#endif

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

    //#if MC < 12109
    @Override
    public Quaternion cameraOrientationCompat() {
        //#if MC > 11404
        return this.cameraOrientation();
        //#else
        //$$ return camera.rotation();
        //#endif
    }
    //#endif
}
