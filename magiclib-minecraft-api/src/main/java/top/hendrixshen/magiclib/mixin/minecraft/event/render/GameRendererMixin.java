package top.hendrixshen.magiclib.mixin.minecraft.event.render;

import net.minecraft.client.renderer.GameRenderer;

// CHECKSTYLE.OFF: ImportOrder
//#if 26.3 > MC && MC > 1.20.6
//$$ import net.minecraft.client.DeltaTracker;
//#endif

//#if MC < 11500
//$$ import net.minecraft.client.Minecraft;
//#endif
// CHECKSTYLE.ON: ImportOrder

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 26.3
import top.hendrixshen.magiclib.libs.com.llamalad7.mixinextras.sugar.Local;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 26.3
import top.hendrixshen.magiclib.util.minecraft.render.RenderUtil;
//#endif

//#if MC >= 12106
//$$ import top.hendrixshen.magiclib.impl.render.context.InWorldGuiDrawer;
//#endif

//#if MC < 11500
//$$ import top.hendrixshen.magiclib.impl.event.EventManager;
//$$ import top.hendrixshen.magiclib.impl.event.minecraft.render.RenderLevelEvent;
//#endif
// CHECKSTYLE.ON: ImportOrder

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {
    //#if MC < 26.3
    @Inject(method = "render", at = @At("HEAD"))
    private void recordPartialTick(
            CallbackInfo ci,
            //#if MC >= 12100
            //$$ @Local(argsOnly = true) DeltaTracker deltaTracker
            //#else
            @Local(argsOnly = true) float partialTick
            //#endif
    ) {
        RenderUtil.setPartialTick(
                //#if MC >= 12100
                //$$ deltaTracker.getGameTimeDeltaPartialTick(false)
                //#else
                partialTick
                //#endif
        );
    }

    @Inject(method = "render", at = @At("TAIL"))
    private void clearPartialTick(CallbackInfo ci) {
        RenderUtil.setPartialTick(1.0F);
    }
    //#endif

    //#if MC >= 12106
    //$$ @Inject(method = "close", at = @At("TAIL"))
    //$$ private void onClose(CallbackInfo ci) {
    //$$     InWorldGuiDrawer.closeInstance();
    //$$ }
    //#endif

    // Moved to LevelRender on mc1.15+
    //#if MC < 11500
    //$$ @Inject(method = "render(FJ)V", at = @At("HEAD"))
    //$$ private void preLevelRender(CallbackInfo ci) {
    //$$     EventManager.dispatch(new RenderLevelEvent.PreRender(RenderLevelEvent.Info.of(Minecraft.getInstance().level)));
    //$$ }
    //$$
    //$$ @Inject(
    //$$         method = "render(FJ)V",
    //$$         at = @At(
    //$$                 value = "CONSTANT",
    //$$                 args = "stringValue=hand"
    //$$         )
    //$$ )
    //$$ private void postLevelRender(float tickDelta, long endTime, CallbackInfo ci) {
    //$$     EventManager.dispatch(new RenderLevelEvent.PreRender(RenderLevelEvent.Info.of(Minecraft.getInstance().level)));
    //$$ }
    //#endif
}
