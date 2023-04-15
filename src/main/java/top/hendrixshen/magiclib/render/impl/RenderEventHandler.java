package top.hendrixshen.magiclib.render.impl;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import top.hendrixshen.magiclib.render.api.IRendererLevelPost;

import java.util.List;

public class RenderEventHandler {
    @Getter
    private static final RenderEventHandler instance = new RenderEventHandler();
    private static final Minecraft mc = Minecraft.getInstance();
    private static final List<IRendererLevelPost> levelPostRenderers = Lists.newArrayList();

    public static void register(IRendererLevelPost renderer) {
        RenderEventHandler.levelPostRenderers.add(renderer);
    }

    public void dispatchPostRenderLevelEvent(PoseStack poseStack) {
        mc.getProfiler().popPush("MagicRenderEventHandler::dispatchRenderWorldPostEvent");
        RenderContext renderContext = new RenderContext(poseStack);
        RenderEventHandler.levelPostRenderers.forEach(renderer -> renderer.render(renderContext));
        mc.getProfiler().pop();
    }
}
