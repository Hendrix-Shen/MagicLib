package top.hendrixshen.magiclib.event.render.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.event.render.impl.RenderEventHandler;
import top.hendrixshen.magiclib.util.MiscUtil;

/**
 * The implementation for mc [1.14.4, ~)
 */
@Environment(EnvType.CLIENT)
@Mixin(GameRenderer.class)
public class MixinGameRenderer {
    @Inject(
            method = "render(FJ)V",
            at = @At(
                    value = "CONSTANT",
                    args = "stringValue=hand"
            )
    )
    private void postRenderLevel(float tickDelta, long endTime, CallbackInfo ci) {
        RenderEventHandler.getInstance().dispatchPostRenderLevelEvent(MiscUtil.cast(Minecraft.getInstance().level), new PoseStack(), tickDelta);
    }
}
