package top.hendrixshen.magiclib.impl.event.minecraft.render;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.event.Event;
import top.hendrixshen.magiclib.api.event.minecraft.render.RenderLevelListener;
import top.hendrixshen.magiclib.api.render.context.RenderContext;

import java.util.List;

//#if MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

public class RenderLevelEvent {
    public static final Minecraft mc = Minecraft.getInstance();

    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LevelRenderContext {
        private final ClientLevel level;
        private final RenderContext renderContext;
        private final float partialTicks;

        public static @NotNull LevelRenderContext of(
                ClientLevel level,
                //#if MC > 11502
                PoseStack poseStack,
                //#endif
                float partialTicks
        ) {
            return new LevelRenderContext(level, RenderContext.of(
                    //#if MC > 11502
                    poseStack
                    //#endif
            ), partialTicks);
        }
    }

    @AllArgsConstructor
    public static class PreRender implements Event<RenderLevelListener> {
        private final LevelRenderContext context;

        @Override
        public void dispatch(@NotNull List<RenderLevelListener> listeners) {
            RenderLevelEvent.mc.getProfiler().push("Magiclib#PreLevelRenderHook");

            for (RenderLevelListener listener : listeners) {
                listener.preRenderLevel(this.context.level, this.context.renderContext, this.context.partialTicks);
            }
            RenderLevelEvent.mc.getProfiler().pop();
        }

        @Override
        public Class<RenderLevelListener> getListenerType() {
            return RenderLevelListener.class;
        }
    }

    @AllArgsConstructor
    public static class PostRender implements Event<RenderLevelListener> {
        private final LevelRenderContext context;

        @Override
        public void dispatch(@NotNull List<RenderLevelListener> listeners) {
            RenderLevelEvent.mc.getProfiler().push("Magiclib#PostLevelRenderHook");

            for (RenderLevelListener listener : listeners) {
                listener.postRenderLevel(this.context.level, this.context.renderContext, this.context.partialTicks);
            }

            RenderLevelEvent.mc.getProfiler().pop();
        }

        @Override
        public Class<RenderLevelListener> getListenerType() {
            return RenderLevelListener.class;
        }
    }
}
