package top.hendrixshen.magiclib.test.compat.minecraft.math;

import com.mojang.math.Matrix4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import top.hendrixshen.magiclib.compat.minecraft.math.Matrix4fCompatApi;
import top.hendrixshen.magiclib.compat.minecraft.math.QuaternionCompatApi;

@Environment(EnvType.CLIENT)
public class TestMatrix4f {

    public static void test() {
        Matrix4f matrix4f = Matrix4fCompatApi.createScaleMatrix(0, 0, 0);
        matrix4f.setIdentityCompat();
        matrix4f.setIdentity();
        matrix4f.multiplyWithTranslationCompat(1, 2, 3);
        matrix4f.multiplyWithTranslation(1, 2, 3);
        matrix4f.multiplyCompat(matrix4f);
        matrix4f.multiply(matrix4f);
        matrix4f.multiplyCompat(QuaternionCompatApi.ONE);
        matrix4f.multiply(QuaternionCompatApi.ONE);
        matrix4f.copyCompat();
        matrix4f.copy();
    }
}
