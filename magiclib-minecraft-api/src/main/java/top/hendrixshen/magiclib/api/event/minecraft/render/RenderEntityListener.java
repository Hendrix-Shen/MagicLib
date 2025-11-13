package top.hendrixshen.magiclib.api.event.minecraft.render;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12106
import org.jetbrains.annotations.ApiStatus;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.world.entity.Entity;

import top.hendrixshen.magiclib.api.event.Listener;
import top.hendrixshen.magiclib.impl.render.context.EntityRenderContext;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12106
import top.hendrixshen.magiclib.api.render.context.RenderContext;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.12.8: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.21.9+        : subproject 1.21.10 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
public interface RenderEntityListener extends Listener {
    //#if MC > 12105
    //$$ void preRenderEntity(Entity entity, EntityRenderContext renderContext);
    //$$
    //$$ void postRenderEntity(Entity entity, EntityRenderContext renderContext);
    //#else
    /**
     * Use {@link RenderEntityListener#preRenderEntity(Entity, EntityRenderContext)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    default void preRenderEntity(Entity entity, RenderContext renderContext, float partialTicks) {
        throw new UnsupportedOperationException("Use preRenderEntity with EntityRenderContext instead");
    }

    /**
     * Use {@link RenderEntityListener#postRenderEntity(Entity, EntityRenderContext)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    default void postRenderEntity(Entity entity, RenderContext renderContext, float partialTicks) {
        throw new UnsupportedOperationException("Use postRenderEntity with EntityRenderContext instead");
    }

    default void preRenderEntity(Entity entity, EntityRenderContext renderContext) {
        throw new UnsupportedOperationException("Implement it before using");
    }

    default void postRenderEntity(Entity entity, EntityRenderContext renderContext) {
        throw new UnsupportedOperationException("Implement it before using");
    }
    //#endif
}
