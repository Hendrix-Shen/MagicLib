package top.hendrixshen.magiclib.api.event.minecraft.render;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12106
import org.jetbrains.annotations.ApiStatus;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.client.multiplayer.ClientLevel;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12106
import net.minecraft.world.level.Level;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.event.Listener;
import top.hendrixshen.magiclib.api.render.context.LevelRenderContext;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12106
import top.hendrixshen.magiclib.api.render.context.RenderContext;
//#endif
// CHECKSTYLE.ON: ImportOrder

public interface RenderLevelListener extends Listener {
    //#if MC > 12105
    //$$ void preRenderLevel(ClientLevel level, LevelRenderContext renderContext);
    //$$
    //$$ void postRenderLevel(ClientLevel level, LevelRenderContext renderContext);
    //#else
    /**
     * Use {@link RenderLevelListener#preRenderLevel(ClientLevel, LevelRenderContext)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    default void preRenderLevel(Level level, RenderContext renderContext, float partialTicks) {
        throw new UnsupportedOperationException("Use preRenderLevel with LevelRenderContext instead");
    }

    /**
     * Use {@link RenderLevelListener#postRenderLevel(ClientLevel, LevelRenderContext)} instead.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    default void postRenderLevel(Level level, RenderContext renderContext, float partialTicks) {
        throw new UnsupportedOperationException("Use postRenderLevel with LevelRenderContext instead");
    }

    default void preRenderLevel(ClientLevel level, LevelRenderContext renderContext) {
        throw new UnsupportedOperationException("Implement it before using");
    }

    default void postRenderLevel(ClientLevel level, LevelRenderContext renderContext) {
        throw new UnsupportedOperationException("Implement it before using");
    }
    //#endif
}
