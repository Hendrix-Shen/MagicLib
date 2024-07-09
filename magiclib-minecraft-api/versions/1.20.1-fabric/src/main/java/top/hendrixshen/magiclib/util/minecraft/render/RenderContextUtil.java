package top.hendrixshen.magiclib.util.minecraft.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import top.hendrixshen.magiclib.mixin.minecraft.accessor.GuiGraphicsAccessor;

public class RenderContextUtil {
    public static GuiGraphics createDrawContext(PoseStack poseStack) {
        GuiGraphics drawContext = new GuiGraphics(Minecraft.getInstance(), RenderUtil.getBufferSource());
        ((GuiGraphicsAccessor) drawContext).magiclib$setPose(poseStack);
        return drawContext;
    }
}
