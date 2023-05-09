package top.hendrixshen.magiclib.render.impl;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import top.hendrixshen.magiclib.render.api.IPostRenderEntityRenderer;
import top.hendrixshen.magiclib.render.api.IPostRenderLevelRenderer;

import java.util.List;

public class RenderEventHandler {
    @Getter
    private static final RenderEventHandler instance = new RenderEventHandler();
    private static final Minecraft mc = Minecraft.getInstance();
    private static final List<IPostRenderEntityRenderer> postRenderEntityRenderers = Lists.newArrayList();
    private static final List<IPostRenderLevelRenderer> postRenderLevelRenderers = Lists.newArrayList();

    public static void registerPostRenderEntityRenderer(IPostRenderEntityRenderer renderer) {
        RenderEventHandler.postRenderEntityRenderers.add(renderer);
    }

    public static void registerPostRenderLevelRenderer(IPostRenderLevelRenderer renderer) {
        RenderEventHandler.postRenderLevelRenderers.add(renderer);
    }

    public void dispatchPostRenderEntityEvent(Entity entity, PoseStack poseStack, float tickDelta) {
        if (!RenderEventHandler.postRenderEntityRenderers.isEmpty()) {
            mc.getProfiler().push("MagicRenderEventHandler::dispatchPostRenderEntityEvent");
            RenderContext renderContext = new RenderContext(poseStack);
            RenderEventHandler.postRenderEntityRenderers.forEach(renderer -> {
                mc.getProfiler().push(String.format("%s::render", renderer.getClass().getName()));
                renderer.render(entity, renderContext, tickDelta);
                mc.getProfiler().pop();
            });
            mc.getProfiler().pop();
        }
    }

    public void dispatchPostRenderLevelEvent(Level level, PoseStack poseStack, float tickDelta) {
        if (!RenderEventHandler.postRenderLevelRenderers.isEmpty()) {
            mc.getProfiler().popPush("MagicRenderEventHandler::dispatchPostRenderLevelEvent");
            RenderContext renderContext = new RenderContext(poseStack);
            RenderEventHandler.postRenderLevelRenderers.forEach(renderer -> {
                mc.getProfiler().push(String.format("%s::render", renderer.getClass().getName()));
                renderer.render(level, renderContext, tickDelta);
                mc.getProfiler().pop();
            });
        }
    }
}
