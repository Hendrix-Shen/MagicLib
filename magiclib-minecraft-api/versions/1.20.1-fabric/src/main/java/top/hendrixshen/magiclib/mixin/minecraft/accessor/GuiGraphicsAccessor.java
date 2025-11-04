package top.hendrixshen.magiclib.mixin.minecraft.accessor;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12106
//$$ import org.joml.Matrix3x2fStack;
//#endif
// CHECKSTYLE.ON: ImportOrder

import net.minecraft.client.gui.GuiGraphics;

// CHECKSTYLE.OFF: ImportOrder
//#if MC < 12106
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.20: subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.20.1+      : subproject 1.20.1        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
    @Mutable
    @Accessor("pose")
    void magiclib$setPose(
            //#if MC >= 12106
            //$$ Matrix3x2fStack matrixStack
            //#else
            PoseStack poseStack
            //#endif
    );
}
