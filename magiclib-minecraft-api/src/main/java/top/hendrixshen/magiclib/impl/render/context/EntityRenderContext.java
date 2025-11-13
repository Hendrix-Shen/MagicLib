package top.hendrixshen.magiclib.impl.render.context;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiComponent;

import top.hendrixshen.magiclib.impl.render.matrix.MinecraftPoseStack;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.12.8: subproject 1.16.5 (main project)        &lt;--------</li>
 * <li>mc1.21.9+        : subproject 1.21.10 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
public class EntityRenderContext extends LevelRenderContextImpl {
    public EntityRenderContext(@NotNull MinecraftPoseStack matrixStack) {
        super(matrixStack);
    }

    @Override
    public @NotNull MinecraftPoseStack getMatrixStack() {
        return (MinecraftPoseStack) super.getMatrixStack();
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    @Override
    public GuiComponent getGuiComponent() {
        throw new UnsupportedOperationException("EntityRenderContext does not support getGuiComponent()");
    }
}
