package top.hendrixshen.magiclib.mixin.minecraft.event.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.hendrixshen.magiclib.impl.event.EventManager;
import top.hendrixshen.magiclib.impl.event.minecraft.render.RenderLevelEvent;
import top.hendrixshen.magiclib.libs.com.llamalad7.mixinextras.sugar.Local;

/**
 * Preprocessor version guide.
 *
 * <li>mc1.14           : subproject 1.14.4 [dummy]</li>
 * <li>mc1.15 ~ mc1.19.3: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.19.4+        : subproject 1.19.4</li>
 */
@Mixin(LevelRenderer.class)
public abstract class LevelRendererMixin {
    @Shadow
    private ClientLevel level;

    @Inject(method = "renderLevel", at = @At("HEAD"))
    private void preRenderLevel(CallbackInfo ci, @Local(argsOnly = true) PoseStack matrixStack) {
        EventManager.dispatch(new RenderLevelEvent.PreRender(RenderLevelEvent.Info.of(
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

    @Inject(
            method = "renderLevel",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/renderer/LevelRenderer;renderDebug(Lnet/minecraft/client/Camera;)V"
            )
    )
    private void postRenderLevel(CallbackInfo ci, @Local(argsOnly = true) PoseStack matrixStack) {
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
