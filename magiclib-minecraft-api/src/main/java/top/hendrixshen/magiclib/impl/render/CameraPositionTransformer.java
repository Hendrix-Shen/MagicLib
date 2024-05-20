package top.hendrixshen.magiclib.impl.render;

import com.mojang.math.Matrix4f;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.render.context.RenderContext;

import java.util.Objects;

//#if MC < 11500
//$$ import com.mojang.blaze3d.platform.GlStateManager;
//$$ import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore">TweakerMore</a>
 */
public class CameraPositionTransformer {
    private final Vec3 pos;

    private RenderContext context;

    public static @NotNull CameraPositionTransformer create(Vec3 pos) {
        return new CameraPositionTransformer(pos);
    }

    private CameraPositionTransformer(Vec3 pos) {
        this.pos = pos;
    }

    /**
     * Pose stack of renderContext will be pushed
     */
    public void apply(@NotNull RenderContext context) {
        this.context = context;
        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();
        Vec3 vec3 = this.pos.subtract(camera.getPosition());
        context.pushPose();
        context.translate(vec3.x(), vec3.y(), vec3.z());
        //#if MC > 11404
        context.mulPoseMatrix(
                //#if MC > 11902
                //$$ new Matrix4f().rotation(camera.rotation())
                //#else
                new Matrix4f(camera.rotation())
                //#endif
        );
        //#else
        //$$ EntityRenderDispatcher entityRenderDispatcher = mc.getEntityRenderDispatcher();
        //$$ GlStateManager.rotatef(-entityRenderDispatcher.playerRotY, 0.0F, 1.0F, 0.0F);
        //$$ GlStateManager.rotatef(entityRenderDispatcher.playerRotX, 1.0F, 0.0F, 0.0F);
        //#endif
    }

    /**
     * Pose stack of renderContext will be popped
     */
    public void restore() {
        if (this.context == null) {
            throw new RuntimeException("CameraPositionTransformer: Calling restore before calling apply");
        }

        this.context.popPose();
        this.context = null;
    }

    public RenderContext getContext() {
        return Objects.requireNonNull(this.context);
    }
}
