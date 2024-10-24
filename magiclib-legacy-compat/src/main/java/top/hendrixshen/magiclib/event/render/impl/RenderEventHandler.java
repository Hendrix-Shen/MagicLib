package top.hendrixshen.magiclib.event.render.impl;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import top.hendrixshen.magiclib.api.compat.minecraft.util.ProfilerCompat;
import top.hendrixshen.magiclib.event.render.api.PostRenderEntityEvent;
import top.hendrixshen.magiclib.event.render.api.PostRenderLevelEvent;

import java.util.List;

public class RenderEventHandler {
    @Getter
    private static final RenderEventHandler instance = new RenderEventHandler();
    private static final List<PostRenderEntityEvent> postRenderEntityEvents = Lists.newArrayList();
    private static final List<PostRenderLevelEvent> postRenderLevelEvents = Lists.newArrayList();

    public static void registerPostRenderEntityEvent(PostRenderEntityEvent event) {
        RenderEventHandler.postRenderEntityEvents.add(event);
    }

    public static void registerPostRenderLevelEvent(PostRenderLevelEvent event) {
        RenderEventHandler.postRenderLevelEvents.add(event);
    }

    public void dispatchPostRenderEntityEvent(Entity entity, PoseStack poseStack, float tickDelta) {
        if (!RenderEventHandler.postRenderEntityEvents.isEmpty()) {
            ProfilerCompat.get().push("MagicRenderEventHandler::dispatchPostRenderEntityEvent");
            RenderContext renderContext = new RenderContext(poseStack);
            RenderEventHandler.postRenderEntityEvents.forEach(event -> {
                ProfilerCompat.get().push(event.getProfilerSectionSupplier());
                event.render(entity, renderContext, tickDelta);
                ProfilerCompat.get().pop();
            });
            ProfilerCompat.get().pop();
        }
    }

    public void dispatchPostRenderLevelEvent(Level level, PoseStack poseStack, float tickDelta) {
        if (!RenderEventHandler.postRenderLevelEvents.isEmpty()) {
            ProfilerCompat.get().popPush("MagicRenderEventHandler::dispatchPostRenderLevelEvent");
            RenderContext renderContext = new RenderContext(poseStack);
            RenderEventHandler.postRenderLevelEvents.forEach(event -> {
                ProfilerCompat.get().push(event.getProfilerSectionSupplier());
                event.render(level, renderContext, tickDelta);
                ProfilerCompat.get().pop();
            });
        }
    }
}
