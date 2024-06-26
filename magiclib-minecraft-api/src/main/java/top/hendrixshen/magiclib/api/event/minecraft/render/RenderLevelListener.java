package top.hendrixshen.magiclib.api.event.minecraft.render;

import net.minecraft.world.level.Level;
import top.hendrixshen.magiclib.api.event.Listener;
import top.hendrixshen.magiclib.api.render.context.RenderContext;

public interface RenderLevelListener extends Listener {
    void preRenderLevel(Level level, RenderContext renderContext, float partialTicks);

    void postRenderLevel(Level level, RenderContext renderContext, float partialTicks);
}
