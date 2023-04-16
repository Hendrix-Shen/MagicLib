package top.hendrixshen.magiclib.render.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.joml.Matrix4f;
import org.spongepowered.asm.mixin.Mixin;

//#if MC > 11404
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Camera;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.render.impl.RenderEventHandler;
import top.hendrixshen.magiclib.util.MiscUtil;
//#else
//$$ import top.hendrixshen.magiclib.compat.preprocess.api.DummyClass;
//#endif

@Environment(EnvType.CLIENT)
//#if MC > 11404
@Mixin(LevelRenderer.class)
//#else
//$$ @Mixin(DummyClass.class)
//#endif
public class MixinLevelRenderer {
    //#if MC > 11404
    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    //#if MC > 11903
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;renderDebug(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/client/Camera;)V"
                    //#else
                    //$$ target = "Lnet/minecraft/client/renderer/LevelRenderer;renderDebug(Lnet/minecraft/client/Camera;)V"
                    //#endif
            )
    )
    private void postRenderLevel(PoseStack poseStack, float tickDelta, long limitTime, boolean renderBlockOutline, Camera camera, GameRenderer gameRenderer, LightTexture lightTexture, Matrix4f matrix4f, CallbackInfo ci) {
        RenderEventHandler.getInstance().dispatchPostRenderLevelEvent(MiscUtil.cast(this), poseStack);
    }
    //#endif
}
