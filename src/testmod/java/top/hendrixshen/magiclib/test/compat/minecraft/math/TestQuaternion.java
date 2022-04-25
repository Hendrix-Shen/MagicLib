package top.hendrixshen.magiclib.test.compat.minecraft.math;

import com.mojang.math.Quaternion;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TestQuaternion {

    public static void test() {
        Quaternion quaternion = new Quaternion(0, 0, 0, 0);
        quaternion.mulCompat(0);
        quaternion.mul(0);
        quaternion.normalizeCompat();
        quaternion.normalize();
        quaternion.copyCompat();
        quaternion.copy();
    }
}
