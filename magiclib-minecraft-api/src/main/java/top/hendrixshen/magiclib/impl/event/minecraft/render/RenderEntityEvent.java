package top.hendrixshen.magiclib.impl.event.minecraft.render;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.util.ProfilerCompat;
import top.hendrixshen.magiclib.api.event.Event;
import top.hendrixshen.magiclib.api.event.minecraft.render.RenderEntityListener;
import top.hendrixshen.magiclib.api.render.context.RenderContext;

import java.util.List;

//#if MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

public class RenderEntityEvent {
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class EntityRenderContext {
        private final Entity entity;
        private final RenderContext renderContext;
        private final float partialTicks;

        public static @NotNull EntityRenderContext of(
                Entity entity,
                //#if MC > 11502
                PoseStack poseStack,
                //#endif
                float partialTicks
        ) {
            return new EntityRenderContext(entity, RenderContext.of(
                    //#if MC > 11502
                    poseStack
                    //#endif
            ), partialTicks);
        }
    }

    @AllArgsConstructor
    public static class PreRender implements Event<RenderEntityListener> {
        private final EntityRenderContext context;

        @Override
        public void dispatch(@NotNull List<RenderEntityListener> listeners) {
            ProfilerCompat.get().push("Magiclib#PreEntityRenderHook");


            for (RenderEntityListener listener : listeners) {
                listener.preRenderEntity(this.context.entity, this.context.renderContext, this.context.partialTicks);
            }

            ProfilerCompat.get().pop();
        }

        @Override
        public Class<RenderEntityListener> getListenerType() {
            return RenderEntityListener.class;
        }
    }

    @AllArgsConstructor
    public static class PostRender implements Event<RenderEntityListener> {
        private final EntityRenderContext context;

        @Override
        public void dispatch(@NotNull List<RenderEntityListener> listeners) {
            ProfilerCompat.get().push("Magiclib#PostEntityRenderHook");

            for (RenderEntityListener listener : listeners) {
                listener.postRenderEntity(this.context.entity, this.context.renderContext, this.context.partialTicks);
            }

            ProfilerCompat.get().pop();
        }

        @Override
        public Class<RenderEntityListener> getListenerType() {
            return RenderEntityListener.class;
        }
    }
}
