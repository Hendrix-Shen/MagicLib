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

//#if MC > 12101
//$$ import net.minecraft.client.renderer.entity.state.EntityRenderState;
//$$ import org.spongepowered.asm.mixin.Unique;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif

//#if MC > 11404
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
//#endif

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    //#if MC > 12101
    //$$ @Unique
    //$$ private Entity magiclib$entity;
    //$$ @Unique
    //$$ private float magiclib$tickDelta;
    //#endif

    @Inject(method = "render",at = @At("HEAD"))
    private void preRenderEntity(
            //#if MC > 12101
            //$$ EntityRenderState entityRenderState,
            //#else
            Entity entity,
            //#if MC < 11500
            //$$ double x,
            //$$ double y,
            //$$ double z,
            //#endif
            float yaw,
            float tickDelta,
            //#endif
            //#if MC > 11404
            PoseStack poseStack,
            MultiBufferSource source,
            int light,
            //#endif
            CallbackInfo ci
    ) {
        EventManager.dispatch(new RenderEntityEvent.PreRender(RenderEntityEvent.EntityRenderContext.of(
                //#if MC > 12101
                //$$ this.magiclib$entity,
                //#else
                entity,
                //#endif
                //#if MC > 11502
                poseStack,
                //#endif
                //#if MC > 12101
                //$$ this.magiclib$tickDelta
                //#else
                tickDelta
                //#endif
        )));
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void postRenderEntity(
            //#if MC > 12101
            //$$ EntityRenderState entityRenderState,
            //#else
            Entity entity,
            //#if MC < 11500
            //$$ double x,
            //$$ double y,
            //$$ double z,
            //#endif
            float yaw,
            float tickDelta,
            //#endif
            //#if MC > 11404
            PoseStack poseStack,
            MultiBufferSource source,
            int light,
            //#endif
            CallbackInfo ci
    ) {
        EventManager.dispatch(new RenderEntityEvent.PostRender(RenderEntityEvent.EntityRenderContext.of(
                //#if MC > 12101
                //$$ this.magiclib$entity,
                //#else
                entity,
                //#endif
                //#if MC > 11502
                poseStack,
                //#endif
                //#if MC > 12101
                //$$ this.magiclib$tickDelta
                //#else
                tickDelta
                //#endif
        )));
    }

    //#if MC > 12101
    //$$ @Inject(method = "createRenderState(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/client/renderer/entity/state/EntityRenderState;", at = @At("HEAD"))
    //$$ private void recordSharedVar(Entity entity, float tickDelta, CallbackInfoReturnable<EntityRenderState> cir) {
    //$$     this.magiclib$entity = entity;
    //$$     this.magiclib$tickDelta = tickDelta;
    //$$ }
    //#endif
}
