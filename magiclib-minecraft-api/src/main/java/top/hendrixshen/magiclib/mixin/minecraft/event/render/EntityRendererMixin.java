package top.hendrixshen.magiclib.mixin.minecraft.event.render;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.world.entity.Entity;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12101
//$$ import net.minecraft.client.renderer.entity.state.EntityRenderState;
//#endif

//#if MC > 11404
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12101
//$$ import org.spongepowered.asm.mixin.Unique;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.event.EventManager;
import top.hendrixshen.magiclib.impl.event.minecraft.render.RenderEntityEvent;
import top.hendrixshen.magiclib.libs.com.llamalad7.mixinextras.sugar.Local;

@Environment(EnvType.CLIENT)
@Mixin(EntityRenderer.class)
public abstract class EntityRendererMixin {
    //#if MC > 12101
    //$$ @Unique
    //$$ private Entity magiclib$entity;
    //#endif

    @Inject(method = "render", at = @At("HEAD"))
    private void preRenderEntity(
            CallbackInfo ci
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            //#if MC < 12102
            , @Local(argsOnly = true) Entity entity
            //#endif
            //#if MC > 11404
            , @Local(argsOnly = true) PoseStack poseStack
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore

    ) {
        EventManager.dispatch(new RenderEntityEvent.PreRender(RenderEntityEvent.Info.of(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                //#if MC > 12101
                //$$ this.magiclib$entity
                //#else
                entity
                //#endif
                //#if MC > 11502
                , poseStack
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        )));
    }

    @Inject(method = "render", at = @At("RETURN"))
    private void postRenderEntity(
            CallbackInfo ci
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            //#if MC < 12102
            , @Local(argsOnly = true) Entity entity
            //#endif
            //#if MC > 11404
            , @Local(argsOnly = true) PoseStack poseStack
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    ) {
        EventManager.dispatch(new RenderEntityEvent.PostRender(RenderEntityEvent.Info.of(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                //#if MC > 12101
                //$$ this.magiclib$entity
                //#else
                entity
                //#endif
                //#if MC > 11502
                , poseStack
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        )));
    }

    //#if MC > 12101
    //$$ @Inject(method = "createRenderState(Lnet/minecraft/world/entity/Entity;F)Lnet/minecraft/client/renderer/entity/state/EntityRenderState;", at = @At("HEAD"))
    //$$ private void recordSharedVar(Entity entity, float tickDelta, CallbackInfoReturnable<EntityRenderState> cir) {
    //$$     this.magiclib$entity = entity;
    //$$ }
    //#endif
}
