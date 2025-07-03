package top.hendrixshen.magiclib.impl.event.minecraft.render;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12004
//$$ import org.joml.Matrix4fStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.client.multiplayer.ClientLevel;

// CHECKSTYLE.OFF: ImportOrder
//#if 12005 > MC && MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.compat.minecraft.util.ProfilerCompat;
import top.hendrixshen.magiclib.api.event.Event;
import top.hendrixshen.magiclib.api.event.minecraft.render.RenderLevelListener;
import top.hendrixshen.magiclib.api.render.context.RenderContext;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12106
import top.hendrixshen.magiclib.util.minecraft.render.RenderUtil;
//#endif

//#if 12106 > MC && MC >= 12006
//$$ import top.hendrixshen.magiclib.impl.render.matrix.JomlMatrixStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import java.util.List;

public class RenderLevelEvent {
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Info {
        @NotNull
        private final ClientLevel level;
        @NotNull
        private final top.hendrixshen.magiclib.api.render.context.LevelRenderContext renderContext;

        public static Info of(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                @NotNull ClientLevel level
                //#if MC >= 12006
                //$$ , @NotNull Matrix4fStack matrixStack
                //#elseif MC >= 11600
                , @NotNull PoseStack matrixStack
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        ) {
            return new Info(
                    level,
                    RenderContext.level(
                            //#if MC >= 11600
                            matrixStack
                            //#endif
                    )
            );
        }
    }

    //#if MC < 12106
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class LevelRenderContext {
        private final ClientLevel level;
        private final RenderContext renderContext;

        public static @NotNull LevelRenderContext of(
                // CHECKSTYLE.OFF: Indentation
                ClientLevel level,
                //#if MC > 12004
                //$$ @NotNull Matrix4fStack matrixStack,
                //#elseif MC >= 11600
                @NotNull PoseStack matrixStack,
                //#endif
                float partialTicks
                // CHECKSTYLE.ON: Indentation
        ) {
            return new LevelRenderContext(level, RenderContext.createWorldRenderContext(
                    //#if MC >= 11600
                    matrixStack
                    //#endif
            ));
        }
    }
    //#endif

    public static class PreRender implements Event<RenderLevelListener> {
        //#if MC < 12106
        private final LevelRenderContext context;
        //#endif
        private final Info info;

        public PreRender(Info info) {
            this.info = info;
            //#if MC < 12106
            this.context = RenderLevelEvent.fromInfo(info);
            //#endif
        }

        @SuppressWarnings("deprecation")
        @Override
        public void dispatch(@NotNull List<RenderLevelListener> listeners) {
            ProfilerCompat.get().push("Magiclib#PreLevelRenderHook");

            for (RenderLevelListener listener : listeners) {
                //#if MC >= 12106
                //$$ listener.preRenderLevel(this.info.level, this.info.renderContext);
                //#else
                try {
                    listener.preRenderLevel(this.info.level, this.info.renderContext);
                } catch (UnsupportedOperationException e) {
                    listener.preRenderLevel(this.context.level, this.context.renderContext, RenderUtil.getPartialTick());
                }
                //#endif
            }

            ProfilerCompat.get().pop();
        }

        @Override
        public Class<RenderLevelListener> getListenerType() {
            return RenderLevelListener.class;
        }
    }

    public static class PostRender implements Event<RenderLevelListener> {
        //#if MC < 12106
        private final LevelRenderContext context;
        //#endif
        private final Info info;

        public PostRender(Info info) {
            this.info = info;
            //#if MC < 12106
            this.context = RenderLevelEvent.fromInfo(info);
            //#endif
        }

        @SuppressWarnings("deprecation")
        @Override
        public void dispatch(@NotNull List<RenderLevelListener> listeners) {
            ProfilerCompat.get().push("Magiclib#PostLevelRenderHook");

            for (RenderLevelListener listener : listeners) {
                //#if MC >= 12106
                //$$ listener.postRenderLevel(this.info.level, this.info.renderContext);
                //#else
                try {
                    listener.postRenderLevel(this.info.level, this.info.renderContext);
                } catch (UnsupportedOperationException e) {
                    listener.postRenderLevel(this.context.level, this.context.renderContext, RenderUtil.getPartialTick());
                }
                //#endif
            }

            ProfilerCompat.get().pop();
        }

        @Override
        public Class<RenderLevelListener> getListenerType() {
            return RenderLevelListener.class;
        }
    }

    //#if MC < 12106
    private static LevelRenderContext fromInfo(Info info) {
        return LevelRenderContext.of(
                info.level,
                //#if MC >= 12006
                //$$ ((JomlMatrixStack) info.renderContext.getMatrixStack()).getRaw(),
                //#elseif MC >= 11600
                info.renderContext.getMatrixStack().getPoseStack(),
                //#endif
                0F
        );
    }
    //#endif
}
