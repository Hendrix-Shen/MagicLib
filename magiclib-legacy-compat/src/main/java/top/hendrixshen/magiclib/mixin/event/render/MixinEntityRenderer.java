package top.hendrixshen.magiclib.mixin.event.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.event.render.impl.RenderEventHandler;

//#if MC > 11404
import net.minecraft.client.renderer.MultiBufferSource;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public class MixinEntityRenderer<T extends Entity> {
    @Inject(
            method = "render",
            at = @At(
                    value = "RETURN"
            )
    )
    private void postRenderEntity(
            T entity,
            //#if MC > 11404
            float yaw,
            float tickDelta,
            PoseStack poseStack,
            MultiBufferSource source,
            int light,
            //#else
            //$$ double x,
            //$$ double y,
            //$$ double z,
            //$$ float yaw,
            //$$ float tickDelta,
            //#endif
            CallbackInfo ci) {
        RenderEventHandler.getInstance().dispatchPostRenderEntityEvent(
                entity,
                //#if MC > 11404
                poseStack,
                //#else
                //$$ new PoseStack(),
                //#endif
                tickDelta
        );
    }
}
