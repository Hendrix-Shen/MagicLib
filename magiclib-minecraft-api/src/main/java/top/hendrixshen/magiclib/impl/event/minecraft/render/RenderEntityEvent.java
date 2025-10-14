package top.hendrixshen.magiclib.impl.event.minecraft.render;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.jetbrains.annotations.NotNull;

import net.minecraft.world.entity.Entity;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.compat.minecraft.util.ProfilerCompat;
import top.hendrixshen.magiclib.api.event.Event;
import top.hendrixshen.magiclib.api.event.minecraft.render.RenderEntityListener;
import top.hendrixshen.magiclib.impl.render.matrix.MinecraftPoseStack;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12106
import top.hendrixshen.magiclib.api.render.context.RenderContext;
import top.hendrixshen.magiclib.util.minecraft.render.RenderUtil;
//#endif
// CHECKSTYLE.ON: ImportOrder

import java.util.List;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.12.8: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.21.9+        : subproject 1.21.10 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
public class RenderEntityEvent {
    @AllArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Info {
        @NotNull
        private final Entity entity;
        @NotNull
        private final top.hendrixshen.magiclib.impl.render.context.EntityRenderContext renderContext;

        public static Info of(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                Entity entity
                //#if MC >= 11600
                , PoseStack poseStack
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        ) {
            return new Info(entity, new top.hendrixshen.magiclib.impl.render.context.EntityRenderContext(new MinecraftPoseStack(
                    //#if MC >= 11600
                    poseStack
                    //#endif
            )));
        }
    }

    //#if MC < 12106
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
    //#endif

    public static class PreRender implements Event<RenderEntityListener> {
        //#if MC < 12106
        private final EntityRenderContext context;
        //#endif
        private final Info info;

        public PreRender(Info info) {
            this.info = info;
            //#if MC < 12106
            this.context = RenderEntityEvent.fromInfo(info);
            //#endif
        }

        @Override
        public void dispatch(@NotNull List<RenderEntityListener> listeners) {
            ProfilerCompat.get().push("Magiclib#PreEntityRenderHook");

            for (RenderEntityListener listener : listeners) {
                //#if MC >= 12106
                //$$ listener.preRenderEntity(this.info.entity, this.info.renderContext);
                //#else
                try {
                    listener.preRenderEntity(this.info.entity, this.info.renderContext);
                } catch (UnsupportedOperationException e) {
                    listener.preRenderEntity(this.context.entity, this.context.renderContext, RenderUtil.getPartialTick());
                }
                //#endif
            }

            ProfilerCompat.get().pop();
        }

        @Override
        public Class<RenderEntityListener> getListenerType() {
            return RenderEntityListener.class;
        }
    }

    public static class PostRender implements Event<RenderEntityListener> {
        //#if MC < 12106
        private final EntityRenderContext context;
        //#endif
        private final Info info;

        public PostRender(Info info) {
            this.info = info;
            //#if MC < 12106
            this.context = RenderEntityEvent.fromInfo(info);
            //#endif
        }

        @Override
        public void dispatch(@NotNull List<RenderEntityListener> listeners) {
            ProfilerCompat.get().push("Magiclib#PostEntityRenderHook");

            for (RenderEntityListener listener : listeners) {
                //#if MC >= 12106
                //$$ listener.postRenderEntity(this.info.entity, this.info.renderContext);
                //#else
                try {
                    listener.postRenderEntity(this.info.entity, this.info.renderContext);
                } catch (UnsupportedOperationException e) {
                    listener.postRenderEntity(this.context.entity, this.context.renderContext, RenderUtil.getPartialTick());
                }
                //#endif
            }

            ProfilerCompat.get().pop();
        }

        @Override
        public Class<RenderEntityListener> getListenerType() {
            return RenderEntityListener.class;
        }
    }

    //#if MC < 12106
    private static EntityRenderContext fromInfo(Info info) {
        return EntityRenderContext.of(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                info.entity,
                //#if MC >= 11600
                info.renderContext.getMatrixStack().getPoseStack(),
                //#endif
                0F
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
        );
    }
    //#endif
}
