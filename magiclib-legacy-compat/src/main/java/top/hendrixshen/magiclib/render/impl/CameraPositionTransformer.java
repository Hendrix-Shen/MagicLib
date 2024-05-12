package top.hendrixshen.magiclib.render.impl;

import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import com.mojang.math.Matrix4f;
import top.hendrixshen.magiclib.api.compat.minecraft.client.CameraCompat;
import top.hendrixshen.magiclib.event.render.impl.RenderContext;

import java.util.Objects;

public class CameraPositionTransformer {
    private final Vec3 pos;
    private RenderContext context;

    public CameraPositionTransformer(Vec3 pos) {
        this.pos = pos;
    }

    public void apply(@NotNull RenderContext context) {
        this.context = context;
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 vec3 = this.pos.subtract(camera.getPosition());
        CameraCompat cameraCompat = CameraCompat.of(camera);
        context.pushPose();
        context.translate(vec3.x(), vec3.y(), vec3.z());
        //#if MC > 11902
        //$$ context.mulPoseMatrix(new Matrix4f().rotation(cameraCompat.rotation().get()));
        //#else
        context.mulPoseMatrix(new Matrix4f(cameraCompat.rotation().get()));
        //#endif
    }

    public void restore() {
        Objects.requireNonNull(this.context);
        this.context.popPose();
        this.context = null;
    }
}
