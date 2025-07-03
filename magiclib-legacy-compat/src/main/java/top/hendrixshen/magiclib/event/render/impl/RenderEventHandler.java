package top.hendrixshen.magiclib.event.render.impl;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import lombok.Getter;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.compat.minecraft.util.ProfilerCompat;
import top.hendrixshen.magiclib.api.event.minecraft.render.RenderEntityListener;
import top.hendrixshen.magiclib.api.event.minecraft.render.RenderLevelListener;
import top.hendrixshen.magiclib.api.render.context.LevelRenderContext;
import top.hendrixshen.magiclib.event.render.api.PostRenderEntityEvent;
import top.hendrixshen.magiclib.event.render.api.PostRenderLevelEvent;
import top.hendrixshen.magiclib.impl.render.context.EntityRenderContext;
import top.hendrixshen.magiclib.impl.render.matrix.MinecraftPoseStack;
import top.hendrixshen.magiclib.util.CommonUtil;
import top.hendrixshen.magiclib.util.minecraft.render.RenderUtil;

import java.util.List;

public class RenderEventHandler implements RenderLevelListener, RenderEntityListener {
    @Getter
    private static final RenderEventHandler instance = CommonUtil.make(() -> {
        RenderEventHandler handler = new RenderEventHandler();
        MagicLib.getInstance().getEventManager().register(RenderLevelListener.class, handler);
        MagicLib.getInstance().getEventManager().register(RenderEntityListener.class, handler);
        return handler;
    });
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
        // NO-OP
    }

    @Override
    public void preRenderEntity(Entity entity, EntityRenderContext renderContext) {
        // NO-OP
    }

    @Override
    public void postRenderEntity(Entity entity, EntityRenderContext renderContext) {
        RenderContext context = new RenderContext(
                //#if MC > 11502
                renderContext.getMatrixStack().getPoseStack()
                //#else
                //$$ new PoseStack()
                //#endif
        );
        RenderEventHandler.postRenderEntityEvents.forEach(event -> {
            ProfilerCompat.get().push(event.getProfilerSectionSupplier());
            event.render(entity, context, RenderUtil.getPartialTick());
            ProfilerCompat.get().pop();
        });
    }

    @Override
    public void preRenderLevel(ClientLevel level, LevelRenderContext renderContext) {
        // NO-OP
    }

    @Override
    public void postRenderLevel(ClientLevel level, LevelRenderContext renderContext) {
        RenderContext context = new RenderContext(
            //#if MC > 11502
            ((MinecraftPoseStack) renderContext.getMatrixStack()).getPoseStack()
            //#else
            //$$ new PoseStack()
            //#endif
        );
        RenderEventHandler.postRenderLevelEvents.forEach(event -> {
            ProfilerCompat.get().push(event.getProfilerSectionSupplier());
            event.render(level, context, RenderUtil.getPartialTick());
            ProfilerCompat.get().pop();
        });
    }
}
