package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// Used in mc1.20.1+
@Environment(EnvType.CLIENT)
@Mixin(DummyClass.class)
public interface GuiGraphicsAccessor {
}
