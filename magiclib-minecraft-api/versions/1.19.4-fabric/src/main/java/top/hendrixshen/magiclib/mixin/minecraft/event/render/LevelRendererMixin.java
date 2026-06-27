package top.hendrixshen.magiclib.mixin.minecraft.event.render;

import net.minecraft.client.renderer.LevelRenderer;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12004
//$$ import org.joml.Matrix4fStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 26.2
//$$ import net.minecraft.client.Minecraft;
//#else
import net.minecraft.client.multiplayer.ClientLevel;
//#endif

//#if MC < 12006
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 26.2
import org.spongepowered.asm.mixin.Shadow;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.event.EventManager;
import top.hendrixshen.magiclib.impl.event.minecraft.render.RenderLevelEvent;
import top.hendrixshen.magiclib.libs.com.llamalad7.mixinextras.sugar.Local;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 : subproject 1.14.4 [dummy]</li>
 * <li>mc1.15+: subproject 1.16.5 (main project)        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    //#if MC < 26.2
    @Shadow
    private ClientLevel level;
    //#endif

    @Inject(
            //#if MC >= 26.2
            //$$ method = "render",
            //#else
            method = "renderLevel",
            //#endif
            at = @At("HEAD")
    )
    private void preRenderLevel(
            CallbackInfo ci
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            //#if MC < 12006
            , @Local(argsOnly = true) PoseStack matrixStack
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    ) {
        EventManager.dispatch(new RenderLevelEvent.PreRender(RenderLevelEvent.Info.of(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                //#if MC >= 26.2
                //$$ Minecraft.getInstance().level
                //#else
                this.level
                //#endif
                //#if MC > 12004
                //$$ , new Matrix4fStack()
                //#elseif MC > 11502
                , matrixStack
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        )));
    }

    @Inject(
            //#if MC >= 26.2
            //$$ method = "render",
            //#else
            method = "renderLevel",
            //#endif
            slice = @Slice(
                    from = @At(
                            //#if MC >= 26.2
                            //$$ value = "INVOKE",
                            //$$ target = "Lnet/minecraft/client/renderer/LevelRenderer;addWeatherPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"
                            //#elseif MC > 12118
                            //$$ value = "INVOKE",
                            //$$ target = "Lnet/minecraft/client/renderer/LevelRenderer;addWeatherPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/world/phys/Vec3;Lcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"
                            //#elseif MC > 12105
                            //$$ value = "INVOKE",
                            //$$ target = "Lnet/minecraft/client/renderer/LevelRenderer;addWeatherPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/world/phys/Vec3;FLcom/mojang/blaze3d/buffers/GpuBufferSlice;)V"
                            //#elseif MC > 12103
                            //$$ value = "INVOKE",
                            //$$ target = "Lnet/minecraft/client/renderer/LevelRenderer;addWeatherPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/world/phys/Vec3;FLnet/minecraft/client/renderer/FogParameters;)V"
                            //#elseif MC > 12102
                            //$$ value = "INVOKE",
                            //$$ target = "Lnet/minecraft/client/renderer/LevelRenderer;addWeatherPass(Lcom/mojang/blaze3d/framegraph/FrameGraphBuilder;Lnet/minecraft/client/renderer/LightTexture;Lnet/minecraft/world/phys/Vec3;FLnet/minecraft/client/renderer/FogParameters;)V"
                            //#else
                            value = "CONSTANT",
                            args = "stringValue=weather",
                            ordinal = 1
                            //#endif
                    )
            ),
            at = @At(
                    value = "INVOKE",
                    //#if MC >= 12006
                    //$$ target = "Lorg/joml/Matrix4fStack;popMatrix()Lorg/joml/Matrix4fStack;",
                    //$$ remap = false,
                    //#else
                    target = "Lcom/mojang/blaze3d/vertex/PoseStack;popPose()V",
                    //#endif
                    ordinal = 0
            )
    )
    private void postRenderLevel(
            CallbackInfo ci,
            @Local(
                    //#if MC < 12006
                    argsOnly = true
                    //#endif
            )
            //#if MC >= 12006
            //$$ Matrix4fStack matrixStack
            //#else
            PoseStack matrixStack
            //#endif
    ) {
        EventManager.dispatch(new RenderLevelEvent.PostRender(RenderLevelEvent.Info.of(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                //#if MC >= 26.2
                //$$ Minecraft.getInstance().level
                //#else
                this.level
                //#endif
                //#if MC > 11502
                , matrixStack
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        )));
    }
}
