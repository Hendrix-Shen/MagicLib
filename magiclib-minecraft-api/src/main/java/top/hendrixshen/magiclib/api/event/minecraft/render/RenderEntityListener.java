package top.hendrixshen.magiclib.api.event.minecraft.render;

import net.minecraft.world.entity.Entity;
import top.hendrixshen.magiclib.api.event.Listener;
import top.hendrixshen.magiclib.api.render.context.RenderContext;

public interface RenderEntityListener extends Listener {
    void preRenderEntity(Entity entity, RenderContext renderContext, float partialTicks);

    void postRenderEntity(Entity entity, RenderContext renderContext, float partialTicks);
}
