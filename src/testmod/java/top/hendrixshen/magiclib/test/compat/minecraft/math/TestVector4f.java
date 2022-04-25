package top.hendrixshen.magiclib.test.compat.minecraft.math;

import com.mojang.math.Matrix4f;
import com.mojang.math.Vector4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class TestVector4f {
    public static void test() {
        Vector4f vector4f = new Vector4f();
        vector4f.transformCompat(new Matrix4f());
        vector4f.transform(new Matrix4f());
    }
}
