package top.hendrixshen.magiclib.compat.mixin.minecraft.client;

import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import net.minecraft.client.Camera;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Mixin(Camera.class)
public class MixinCamera {
    @Shadow
    private float yRot;

    @Shadow
    private float xRot;

    @Remap("method_23767")
    public Quaternion rotation() {
        Quaternion ret = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
        ret.mul(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), -this.yRot, true));
        ret.mul(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F), this.xRot, true));
        return ret;
    }
}
