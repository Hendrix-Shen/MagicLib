package top.hendrixshen.magiclib.render.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.world.level.Level;
import top.hendrixshen.magiclib.render.impl.RenderContext;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface IPostRenderLevelRenderer {
    void render(Level level, RenderContext context, float tickDelta);
}
