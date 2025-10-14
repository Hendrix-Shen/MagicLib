package top.hendrixshen.magiclib.mixin.minecraft.event.render;

import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11404
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.hendrixshen.magiclib.impl.event.EventManager;
import top.hendrixshen.magiclib.impl.event.minecraft.render.RenderEntityEvent;
import top.hendrixshen.magiclib.libs.com.llamalad7.mixinextras.sugar.Local;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.12.8: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.21.9+        : subproject 1.21.10 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(EntityRenderDispatcher.class)
public abstract class EntityRenderDispatcherMixin {
    @Inject(
            //#if MC > 12101
            //$$ method = "render(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V",
            //#elseif MC > 11404
            method = "render",
            //#else
            //$$ method = "render(Lnet/minecraft/world/entity/Entity;FZ)V",
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC > 12104
                    //$$ target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V"
                    //#elseif MC > 12101
                    //$$ target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
                    //#elseif MC > 11404
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V"
                    //#else
                    //$$ target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;render(Lnet/minecraft/world/entity/Entity;DDDFFZ)V"
                    //#endif
            )
    )
    private void preRenderEntity(
            CallbackInfo ci
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            , @Local(argsOnly = true) Entity entity
            //#if MC > 11404
            , @Local(argsOnly = true) PoseStack poseStack
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    ) {
        EventManager.dispatch(new RenderEntityEvent.PreRender(RenderEntityEvent.Info.of(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                entity
                //#if MC > 11502
                , poseStack
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        )));
    }

    @Inject(
            //#if MC > 12101
            //$$ method = "render(Lnet/minecraft/world/entity/Entity;DDDFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V",
            //#elseif MC > 11404
            method = "render",
            //#else
            //$$ method = "render(Lnet/minecraft/world/entity/Entity;FZ)V",
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC > 12104
                    //$$ target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;DDDLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;ILnet/minecraft/client/renderer/entity/EntityRenderer;)V",
                    //#elseif MC > 12101
                    //$$ target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/client/renderer/entity/state/EntityRenderState;Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
                    //#elseif MC > 11404
                    target = "Lnet/minecraft/client/renderer/entity/EntityRenderer;render(Lnet/minecraft/world/entity/Entity;FFLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;I)V",
                    //#else
                    //$$ target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;render(Lnet/minecraft/world/entity/Entity;DDDFFZ)V",
                    //#endif
                    shift = At.Shift.AFTER
            )
    )
    private void postRenderEntity(
            CallbackInfo ci
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            , @Local(argsOnly = true) Entity entity
            //#if MC > 11404
            , @Local(argsOnly = true) PoseStack poseStack
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    ) {
        EventManager.dispatch(new RenderEntityEvent.PostRender(RenderEntityEvent.Info.of(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                entity
                //#if MC > 11502
                , poseStack
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        )));
    }
}
