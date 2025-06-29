package top.hendrixshen.magiclib.util.minecraft.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12106
import com.mojang.blaze3d.vertex.PoseStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12106
//$$ import top.hendrixshen.magiclib.mixin.minecraft.accessor.GameRendererAccessor;
//#else
import top.hendrixshen.magiclib.mixin.minecraft.accessor.GuiGraphicsAccessor;
//#endif
// CHECKSTYLE.ON: ImportOrder

// Used in mc1.20.1+
public class RenderContextUtil {
    //#if MC >= 12106
    //$$ public static GuiGraphics createDefaultGuiGraphics() {
    //$$     Minecraft minecraft = Minecraft.getInstance();
    //$$     return new GuiGraphics(minecraft, ((GameRendererAccessor) minecraft.gameRenderer).magiclib$getGuiRenderState());
    //$$ }
    //#else
    /**
     * @deprecated Use {@link #createGuiGraphic(PoseStack)} instead.
     */
    @Deprecated
    public static GuiGraphics createDrawContext(PoseStack poseStack) {
        return createGuiGraphic(poseStack);
    }

    public static GuiGraphics createGuiGraphic(PoseStack poseStack) {
        GuiGraphics guiGraphics = new GuiGraphics(Minecraft.getInstance(), RenderUtil.getBufferSource());
        ((GuiGraphicsAccessor) guiGraphics).magiclib$setPose(poseStack);
        return guiGraphics;
    }
    //#endif
}
