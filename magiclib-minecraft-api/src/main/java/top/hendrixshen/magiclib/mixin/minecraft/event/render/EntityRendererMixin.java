package top.hendrixshen.magiclib.mixin.minecraft.event.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.impl.event.EventManager;
import top.hendrixshen.magiclib.impl.event.minecraft.render.RenderEntityEvent;

//#if MC > 11404
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public class EntityRendererMixin<T extends Entity> {
    @Inject(
            method = "render",
            at = @At(
                    value = "HEAD"
            )
    )
    private void preRenderEntity(
            T entity,
            //#if MC < 11500
            //$$ double x,
            //$$ double y,
            //$$ double z,
            //#endif
            float yaw,
            float tickDelta,
            //#if MC > 11404
            PoseStack poseStack,
            MultiBufferSource source,
            int light,
            //#endif
            CallbackInfo ci
    ) {
        EventManager.dispatch(new RenderEntityEvent.PreRender(RenderEntityEvent.EntityRenderContext.of(
                entity,
                //#if MC > 11502
                poseStack,
                //#endif
                tickDelta
        )));
    }

    @Inject(
            method = "render",
            at = @At(
                    value = "RETURN"
            )
    )
    private void postRenderEntity(
            T entity,
            //#if MC < 11500
            //$$ double x,
            //$$ double y,
            //$$ double z,
            //#endif
            float yaw,
            float tickDelta,
            //#if MC > 11404
            PoseStack poseStack,
            MultiBufferSource source,
            int light,
            //#endif
            CallbackInfo ci
    ) {
        EventManager.dispatch(new RenderEntityEvent.PostRender(RenderEntityEvent.EntityRenderContext.of(
                entity,
                //#if MC > 11502
                poseStack,
                //#endif
                tickDelta
        )));
    }
}
