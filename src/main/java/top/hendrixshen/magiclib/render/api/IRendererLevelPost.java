package top.hendrixshen.magiclib.render.api;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import top.hendrixshen.magiclib.render.impl.RenderContext;

@Environment(EnvType.CLIENT)
@FunctionalInterface
public interface IRendererLevelPost {
    void render(RenderContext context);
}
