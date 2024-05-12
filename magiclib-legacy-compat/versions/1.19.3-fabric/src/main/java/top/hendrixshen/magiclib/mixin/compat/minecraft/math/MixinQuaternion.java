package top.hendrixshen.magiclib.mixin.compat.minecraft.math;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.api.preprocess.DummyClass;

@Environment(EnvType.CLIENT)
@Mixin(DummyClass.class)
public class MixinQuaternion {
}