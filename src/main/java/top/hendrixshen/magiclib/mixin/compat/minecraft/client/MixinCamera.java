package top.hendrixshen.magiclib.mixin.compat.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.client.CameraCompatApi;

//#if MC < 11500
//$$ import com.mojang.math.Vector3f;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public abstract class MixinCamera implements CameraCompatApi {
    //#if MC > 11404
    @Shadow
    public abstract Quaternionf rotation();
    //#else
    //$$ @Shadow
    //$$ private float yRot;
    //$$
    //$$ @Shadow
    //$$ private float xRot;
    //#endif

    @Override
    public Quaternionf rotationCompat() {
        //#if MC > 11404
        return this.rotation();
        //#else
        //$$ Quaternion ret = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
        //$$ ret.mul(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), -this.yRot, true));
        //$$ ret.mul(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F), this.xRot, true));
        //$$ return ret;
        //#endif
    }
}
