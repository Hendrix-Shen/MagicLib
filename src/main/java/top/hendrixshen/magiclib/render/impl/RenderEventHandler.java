package top.hendrixshen.magiclib.render.impl;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;
import top.hendrixshen.magiclib.render.api.IPostRenderLevelRenderer;

import java.util.List;

public class RenderEventHandler {
    @Getter
    private static final RenderEventHandler instance = new RenderEventHandler();
    private static final Minecraft mc = Minecraft.getInstance();
    private static final List<IPostRenderLevelRenderer> postRenderLevelRenderers = Lists.newArrayList();

    public static void registerPostRenderLevelRenderer(IPostRenderLevelRenderer renderer) {
        RenderEventHandler.postRenderLevelRenderers.add(renderer);
    }

    public void dispatchPostRenderLevelEvent(Level level, PoseStack poseStack) {
        mc.getProfiler().popPush("MagicRenderEventHandler::dispatchRenderWorldPostEvent");
        RenderContext renderContext = new RenderContext(poseStack);
        RenderEventHandler.postRenderLevelRenderers.forEach(renderer -> renderer.render(level, renderContext));
        mc.getProfiler().pop();
    }
}
