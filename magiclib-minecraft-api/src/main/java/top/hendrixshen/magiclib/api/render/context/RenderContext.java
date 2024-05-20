package top.hendrixshen.magiclib.api.render.context;

import com.mojang.math.Matrix4f;
import net.minecraft.client.gui.GuiComponent;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.render.context.RenderContextImpl;

//#if MC > 11904
//$$ import com.mojang.blaze3d.vertex.Tesselator;
//$$ import net.minecraft.client.Minecraft;
//$$ import net.minecraft.client.renderer.MultiBufferSource;
//$$ import top.hendrixshen.magiclib.mixin.minecraft.accessor.GuiGraphicsAccessor;
//#endif

//#if MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore">TweakerMore</a>
 */
public interface RenderContext {
    static @NotNull RenderContext of(
            //#if MC > 11502
            @NotNull PoseStack poseStack
            //#endif
    ) {
        //#if MC > 11904
        //$$ GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance(),
        //$$         MultiBufferSource.immediate(Tesselator.getInstance().getBuilder()));
        //$$ ((GuiGraphicsAccessor) guiGraphics).magiclib$setPose(poseStack);
        //#endif

        return new RenderContextImpl(
                //#if MC > 11904
                //$$ guiGraphics,
                //#endif
                //#if MC > 11502
                poseStack
                //#endif
        );
    }

    //#if MC > 11904
    //$$ static RenderContext of(@NotNull GuiGraphics guiGraphics) {
    //$$ 	return new RenderContextImpl(guiGraphics, guiGraphics.pose());
    //$$ }
    //#endif

    GuiComponent getGuiComponent();

    //#if MC > 11502
    PoseStack getPoseStack();
    //#endif

    void pushPose();

    void popPose();

    void translate(double x, double y, double z);

    void scale(double x, double y, double z);

    void mulPoseMatrix(Matrix4f matrix4f);
}
