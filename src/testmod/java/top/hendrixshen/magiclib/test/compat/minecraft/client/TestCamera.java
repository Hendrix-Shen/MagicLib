package top.hendrixshen.magiclib.test.compat.minecraft.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;

@Environment(EnvType.CLIENT)
public class TestCamera {
    public static void test() {
        Camera camera = new Camera();
        camera.rotationCompat();
        camera.rotation();
    }
}
