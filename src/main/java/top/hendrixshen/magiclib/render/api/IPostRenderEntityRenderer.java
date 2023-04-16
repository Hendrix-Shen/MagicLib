package top.hendrixshen.magiclib.render.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.entity.Entity;
import top.hendrixshen.magiclib.render.impl.RenderContext;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface IPostRenderEntityRenderer {
    void render(Entity entity, RenderContext context, float tickDelta);
}
