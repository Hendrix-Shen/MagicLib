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

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

// Used in mc1.20.1+
@Environment(EnvType.CLIENT)
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
