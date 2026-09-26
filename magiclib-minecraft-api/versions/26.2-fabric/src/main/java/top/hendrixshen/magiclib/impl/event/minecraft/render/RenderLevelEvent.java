package top.hendrixshen.magiclib.impl.event.minecraft.render;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4fStack;

import net.minecraft.client.multiplayer.ClientLevel;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.compat.minecraft.util.ProfilerCompat;
import top.hendrixshen.magiclib.api.event.Event;
import top.hendrixshen.magiclib.api.event.minecraft.render.RenderLevelListener;
import top.hendrixshen.magiclib.api.render.context.RenderContext;
import top.hendrixshen.magiclib.impl.render.text.TextRenderBatch;

import java.util.List;

/**
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc26.1: subproject 1.16.5 (main project)</li>
 * <li>mc26.2+        : subproject 26.2        &lt;--------</li>
 */
public class RenderLevelEvent {
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Info {
        @NotNull
        private final ClientLevel level;
        @NotNull
        private final top.hendrixshen.magiclib.api.render.context.LevelRenderContext renderContext;

        public static Info of(@NotNull ClientLevel level, @NotNull Matrix4fStack matrixStack) {
            return new Info(
                    level,
                    RenderContext.level(matrixStack)
            );
        }
    }

    public static class PreRender implements Event<RenderLevelListener> {
        private final Info info;

        public PreRender(Info info) {
            this.info = info;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void dispatch(@NotNull List<RenderLevelListener> listeners) {
            ProfilerCompat.get().push("Magiclib#PreLevelRenderHook");

            // A batch scope is closed by its owner, so a leftover batch can only be left behind by a renderer that
            // threw. Submit it before the level frame graph of this frame is built.
            try {
                TextRenderBatch.flushActiveBatch();
            } catch (Throwable throwable) {
                MagicLib.getLogger().warn("Failed to submit a leftover MagicLib text render batch.", throwable);
            }

            for (RenderLevelListener listener : listeners) {
                listener.preRenderLevel(this.info.level, this.info.renderContext);
            }

            ProfilerCompat.get().pop();
        }

        @Override
        public Class<RenderLevelListener> getListenerType() {
            return RenderLevelListener.class;
        }
    }

    public static class PostRender implements Event<RenderLevelListener> {
        private final Info info;

        public PostRender(Info info) {
            this.info = info;
        }

        @SuppressWarnings("deprecation")
        @Override
        public void dispatch(@NotNull List<RenderLevelListener> listeners) {
            ProfilerCompat.get().push("Magiclib#PostLevelRenderHook");
            // This hook runs after the level frame graph finished executing and while the model view matrix still
            // contains the camera transformation, so all text drawn by the listeners joins one batch which is
            // submitted in a single upload and draw pass, without relying on the immediate graphics state.
            TextRenderBatch.beginBatch();

            try {
                for (RenderLevelListener listener : listeners) {
                    listener.postRenderLevel(this.info.level, this.info.renderContext);
                }
            } finally {
                try {
                    TextRenderBatch.endBatch();
                } catch (Throwable throwable) {
                    MagicLib.getLogger().warn("Failed to submit the MagicLib text render batch.", throwable);
                }
            }

            ProfilerCompat.get().pop();
        }

        @Override
        public Class<RenderLevelListener> getListenerType() {
            return RenderLevelListener.class;
        }
    }
}
