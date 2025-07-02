package top.hendrixshen.magiclib.api.event.minecraft.render;

import org.jetbrains.annotations.ApiStatus;

import net.minecraft.world.entity.Entity;

import top.hendrixshen.magiclib.api.event.Listener;
import top.hendrixshen.magiclib.api.render.context.RenderContext;
import top.hendrixshen.magiclib.impl.render.context.EntityRenderContext;

public interface RenderEntityListener extends Listener {
    //#if MC < 12106
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    default void preRenderEntity(Entity entity, RenderContext renderContext, float partialTicks) {
        throw new UnsupportedOperationException("Use preRenderEntity with EntityRenderContext instead");
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    default void postRenderEntity(Entity entity, RenderContext renderContext, float partialTicks) {
        throw new UnsupportedOperationException("Use postRenderEntity with EntityRenderContext instead");
    }
    //#endif

    default void preRenderEntity(Entity entity, EntityRenderContext renderContext) {
        throw new UnsupportedOperationException("Implement it before using");
    }

    default void postRenderEntity(Entity entity, EntityRenderContext renderContext) {
        throw new UnsupportedOperationException("Implement it before using");
    }
}
