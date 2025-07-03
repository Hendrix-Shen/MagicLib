package top.hendrixshen.magiclib.impl.render.context;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import net.minecraft.client.gui.GuiComponent;

import top.hendrixshen.magiclib.impl.render.matrix.MinecraftPoseStack;

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
