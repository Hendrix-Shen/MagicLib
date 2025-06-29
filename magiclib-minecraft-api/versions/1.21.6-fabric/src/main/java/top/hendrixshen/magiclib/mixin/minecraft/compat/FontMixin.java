package top.hendrixshen.magiclib.mixin.minecraft.compat;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.fake.compat.FontAccessor;
import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// Used in mc1.21.5-
@Environment(EnvType.CLIENT)
@Mixin(DummyClass.class)
public abstract class FontMixin implements FontAccessor {
}
