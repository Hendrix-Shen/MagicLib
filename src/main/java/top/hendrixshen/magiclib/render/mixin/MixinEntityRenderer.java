package top.hendrixshen.magiclib.render.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.render.impl.RenderEventHandler;

//#if MC > 11404
import net.minecraft.client.renderer.MultiBufferSource;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public class MixinEntityRenderer<T extends Entity> {
    @Inject(method = "render", at = @At(value = "RETURN"))
    //#if MC > 11404
    private void postRenderEntity(T entity, float yaw, float tickDelta, PoseStack poseStack, MultiBufferSource source, int light, CallbackInfo ci) {
    //#else
    //$$ private void postRenderEntity(T entity, double x, double y, double z, float yaw, float tickDelta, CallbackInfo ci) {
    //$$     PoseStack poseStack = new PoseStack();
    //#endif
        RenderEventHandler.getInstance().dispatchPostRenderEntityEvent(entity, poseStack, tickDelta);
    }
}
