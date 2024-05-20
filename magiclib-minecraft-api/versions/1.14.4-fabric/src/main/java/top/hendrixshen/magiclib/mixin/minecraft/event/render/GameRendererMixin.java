package top.hendrixshen.magiclib.mixin.minecraft.event.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.api.preprocess.DummyClass;
import top.hendrixshen.magiclib.impl.event.EventManager;
import top.hendrixshen.magiclib.impl.event.minecraft.render.RenderLevelEvent;

@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class GameRendererMixin {
    @Inject(
            method = "render(FJ)V",
            at = @At(
                    value = "HEAD"
            )
    )
    private void preLevelRender(float tickDelta, long endTime, CallbackInfo ci) {
        EventManager.dispatch(new RenderLevelEvent.PreRender(RenderLevelEvent.LevelRenderContext.of(
                Minecraft.getInstance().level,
                tickDelta
        )));
    }

    @Inject(
            method = "render(FJ)V",
            at = @At(
                    value = "CONSTANT",
                    args = "stringValue=hand"
            )
    )
    private void postLevelRender(float tickDelta, long endTime, CallbackInfo ci) {
        EventManager.dispatch(new RenderLevelEvent.PostRender(RenderLevelEvent.LevelRenderContext.of(
                Minecraft.getInstance().level,
                tickDelta
        )));
    }
}
