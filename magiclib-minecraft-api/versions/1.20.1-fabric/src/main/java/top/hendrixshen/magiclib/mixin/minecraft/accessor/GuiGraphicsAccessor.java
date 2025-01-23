package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Environment(EnvType.CLIENT)
@Mixin(GuiGraphics.class)
public interface GuiGraphicsAccessor {
    @Mutable
    @Accessor("pose")
    void magiclib$setPose(PoseStack poseStack);
}
