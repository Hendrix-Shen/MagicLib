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

//#if MC > 12101
//$$ import com.mojang.blaze3d.resource.GraphicsResourceAllocator;
//#endif

//#if MC > 12006
//$$ import net.minecraft.client.DeltaTracker;
//#endif

//#if MC > 12004
//$$ import org.joml.Matrix4fStack;
//$$ import top.hendrixshen.magiclib.libs.com.llamalad7.mixinextras.sugar.Local;
//#endif

//#if MC > 11903
//$$ import org.spongepowered.asm.mixin.injection.Slice;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow
    private ClientLevel level;

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void preRenderLevel(
            //#if MC > 12101
            //$$ GraphicsResourceAllocator graphicsResourceAllocator,
            //#endif
            //#if MC > 12006
            //$$ DeltaTracker deltaTracker,
            //#else
            //#if MC < 12005
            PoseStack matrixStack,
            //#endif
            float tickDelta,
            long limitTime,
            //#endif
            boolean renderBlockOutline,
            Camera camera,
            GameRenderer gameRenderer,
            LightTexture lightTexture,
            Matrix4f frustumMatrix,
            //#if MC > 12004
            //$$ Matrix4f projectionMatrix,
            //#endif
            CallbackInfo ci
    ) {
        EventManager.dispatch(new RenderLevelEvent.PreRender(RenderLevelEvent.LevelRenderContext.of(
                this.level,
                //#if MC > 12004
                //$$ new Matrix4fStack(),
                //#elseif MC > 11502
                matrixStack,
                //#endif
                //#if MC > 12006
                //$$ deltaTracker.getGameTimeDeltaPartialTick(false)
                //#else
                tickDelta
                //#endif
        )));
    }

    @Inject(
            method = "renderLevel",
            //#if MC > 11903
            //$$ slice = @Slice(
            //$$         from = @At(
            //$$                 value = "CONSTANT",
            //#if MC > 12101
            //$$                 args = "stringValue=framegraph",
            //$$                 ordinal = 0
            //#else
            //$$                 args = "stringValue=weather",
            //$$                 ordinal = 1
            //#endif
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
            //#if MC > 12101
            //$$ GraphicsResourceAllocator graphicsResourceAllocator,
            //#endif
            //#if MC > 12006
            //$$ DeltaTracker deltaTracker,
            //#else
            //#if MC < 12005
            PoseStack matrixStack,
            //#endif
            float tickDelta,
            long limitTime,
            //#endif
            boolean renderBlockOutline,
            Camera camera,
            GameRenderer gameRenderer,
            LightTexture lightTexture,
            Matrix4f frustumMatrix,
            //#if MC > 12004
            //$$ Matrix4f projectionMatrix,
            //#endif
            CallbackInfo ci
            //#if MC > 12004
            //$$ , @Local Matrix4fStack matrixStack
            //#endif
    ) {
        EventManager.dispatch(new RenderLevelEvent.PostRender(RenderLevelEvent.LevelRenderContext.of(
                this.level,
                //#if MC > 11502
                matrixStack,
                //#endif
                //#if MC > 12006
                //$$ deltaTracker.getGameTimeDeltaPartialTick(false)
                //#else
                tickDelta
                //#endif
        )));
    }
}
