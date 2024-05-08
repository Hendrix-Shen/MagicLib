package top.hendrixshen.magiclib.impl.compat.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.client.CameraCompat;
import top.hendrixshen.magiclib.api.compat.mojang.math.QuaternionCompat;

//#if MC < 11500
//$$ import com.mojang.math.Quaternion;
//$$ import com.mojang.math.Vector3f;
//#endif

@Environment(EnvType.CLIENT)
public class CameraCompatImpl extends AbstractCompat<Camera> implements CameraCompat {
    public CameraCompatImpl(@NotNull Camera type) {
        super(type);
    }

    @Override
    public QuaternionCompat rotation() {
        //#if MC > 11404
        return QuaternionCompat.of(this.get().rotation());
        //#else
        //$$ Quaternion quaternion = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
        //$$ Quaternion ret = new Quaternion(0.0F, 0.0F, 0.0F, 1.0F);
        //$$ ret.mul(new Quaternion(new Vector3f(0.0F, 1.0F, 0.0F), -this.get().getYRot(), true));
        //$$ ret.mul(new Quaternion(new Vector3f(1.0F, 0.0F, 0.0F), this.get().getXRot(), true));
        //$$ return QuaternionCompat.of(quaternion);
        //#endif
    }
}
