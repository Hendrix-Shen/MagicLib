package top.hendrixshen.magiclib.render.mixin;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;

//#if MC > 11404
import top.hendrixshen.magiclib.compat.preprocess.api.DummyClass;
//#else
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//$$ import net.minecraft.client.Camera;
//$$ import net.minecraft.client.renderer.GameRenderer;
//$$ import org.spongepowered.asm.mixin.Final;
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
//$$ import top.hendrixshen.magiclib.compat.minecraft.api.math.Vector3fCompatApi;
//$$ import top.hendrixshen.magiclib.render.impl.RenderEventHandler;
//#endif

@Environment(EnvType.CLIENT)
//#if MC > 11404
@Mixin(DummyClass.class)
//#else
//$$ @Mixin(GameRenderer.class)
//#endif
public class MixinGameRenderer {
    //#if MC < 11500
    //$$ @Shadow
    //$$ @Final
    //$$ private Camera mainCamera;
    //$$
    //$$ @Inject(
    //$$         method = "render(FJ)V",
    //$$         at = @At(
    //$$                 value = "CONSTANT",
    //$$                 args = "stringValue=hand"
    //$$         )
    //$$ )
    //$$ private void postRenderLevel(float tickDelta, long endTime, CallbackInfo ci) {
    //$$     RenderEventHandler.getInstance().dispatchPostRenderLevelEvent(new PoseStack());
    //$$ }
    //#endif
}
