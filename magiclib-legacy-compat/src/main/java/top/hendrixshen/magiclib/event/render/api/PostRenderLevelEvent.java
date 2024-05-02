package top.hendrixshen.magiclib.event.render.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.Level;
import top.hendrixshen.magiclib.event.render.impl.RenderContext;

@FunctionalInterface
@Environment(EnvType.CLIENT)
public interface PostRenderLevelEvent extends IRenderEvent<Level> {
    @Override
    void render(Level level, RenderContext context, float tickDelta);
}
