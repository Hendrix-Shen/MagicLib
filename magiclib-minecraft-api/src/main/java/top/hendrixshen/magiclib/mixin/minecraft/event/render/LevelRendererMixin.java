package top.hendrixshen.magiclib.mixin.minecraft.event.render;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12004
//$$ import org.joml.Matrix4fStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12006
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11903
//$$ import org.spongepowered.asm.mixin.injection.Slice;
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
    @Shadow
    private ClientLevel level;

    @Inject(method = "renderLevel", at = @At("HEAD"))
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
                this.level
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
                this.level
                //#if MC > 11502
                , matrixStack
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        )));
    }
}
