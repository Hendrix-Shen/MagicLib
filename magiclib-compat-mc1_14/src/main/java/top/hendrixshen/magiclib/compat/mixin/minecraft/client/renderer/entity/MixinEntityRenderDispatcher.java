package top.hendrixshen.magiclib.compat.mixin.minecraft.client.renderer.entity;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Mixin(EntityRenderDispatcher.class)
public class MixinEntityRenderDispatcher {
    @Shadow
    public Camera camera;

    @Remap("method_23168")
    public double distanceToSqr(Entity entity) {
        return this.camera.getPosition().distanceToSqr(entity.position());
    }

    @Remap("method_24197")
    public Quaternion cameraOrientation() {
        Quaternion ret = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
        ret.mul(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), -this.camera.getYRot(), true));
        ret.mul(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F), this.camera.getXRot(), true));
        return ret;
    }
}
