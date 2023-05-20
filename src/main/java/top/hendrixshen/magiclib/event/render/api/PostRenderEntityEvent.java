package top.hendrixshen.magiclib.event.render.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Entity;
import top.hendrixshen.magiclib.event.render.impl.RenderContext;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface PostRenderEntityEvent extends IRenderEvent<Entity> {
    @Override
    void render(Entity entity, RenderContext context, float tickDelta);
}
