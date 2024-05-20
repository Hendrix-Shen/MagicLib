package top.hendrixshen.magiclib.mixin.minecraft.event.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Matrix4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Camera;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.LightTexture;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.impl.event.EventManager;
import top.hendrixshen.magiclib.impl.event.minecraft.render.RenderLevelEvent;

//#if MC > 11903
//$$ import org.spongepowered.asm.mixin.injection.Slice;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
    @Shadow
    private ClientLevel level;

    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "HEAD"
            )
    )
    private void preRenderLevel(
            //#if MC < 12005
            PoseStack poseStack,
            //#endif
            float tickDelta,
            long limitTime,
            boolean renderBlockOutline,
            Camera camera,
            GameRenderer gameRenderer,
            LightTexture lightTexture,
            Matrix4f matrix4f,
            //#if MC > 12004
            //$$ Matrix4f matrix4f2,
            //#endif
            CallbackInfo ci
    ) {
        EventManager.dispatch(new RenderLevelEvent.PreRender(RenderLevelEvent.LevelRenderContext.of(
                this.level,
                //#if MC > 12004
                //$$ new PoseStack(),
                //#elseif MC > 11502
                poseStack,
                //#endif
                tickDelta
        )));
    }

    @Inject(
            method = "renderLevel",
            //#if MC > 11903
            //$$ slice = @Slice(
            //$$         from = @At(
            //$$                 value = "CONSTANT",
            //$$                 args = "stringValue=weather",
            //$$                 ordinal = 1
            //$$         )
            //$$ ),
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC > 11903
                    //#if MC > 12004
                    //$$ target = "Lorg/joml/Matrix4fStack;popMatrix()Lorg/joml/Matrix4fStack;",
                    //$$ remap = false,
                    //#else
                    //$$ target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V",
                    //#endif
                    //$$ ordinal = 0
                    //#else
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;renderDebug(Lnet/minecraft/client/Camera;)V"
                    //#endif
            )
    )
    private void postRenderLevel(
            //#if MC < 12005
            PoseStack poseStack,
            //#endif
            float tickDelta,
            long limitTime,
            boolean renderBlockOutline,
            Camera camera,
            GameRenderer gameRenderer,
            LightTexture lightTexture,
            Matrix4f matrix4f,
            //#if MC > 12004
            //$$ Matrix4f matrix4f2,
            //#endif
            CallbackInfo ci
    ) {
        EventManager.dispatch(new RenderLevelEvent.PostRender(RenderLevelEvent.LevelRenderContext.of(
                this.level,
                //#if MC > 12004
                //$$ new PoseStack(),
                //#elseif MC > 11502
                poseStack,
                //#endif
                tickDelta
        )));
    }
}
