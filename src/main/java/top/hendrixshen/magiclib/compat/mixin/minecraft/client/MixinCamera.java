package top.hendrixshen.magiclib.compat.mixin.minecraft.client;

import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.client.CameraCompatApi;

//#if MC <= 11404
//$$ import com.mojang.math.Vector3f;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(Camera.class)
public abstract class MixinCamera implements CameraCompatApi {


    //#if MC > 11404
    @Shadow
    public abstract Quaternion rotation();
    //#else
    //$$ @Shadow
    //$$ private float yRot;

    //$$ @Shadow
    //$$ private float xRot;
    //#endif

    @Override
    public Quaternion rotationCompat() {
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
