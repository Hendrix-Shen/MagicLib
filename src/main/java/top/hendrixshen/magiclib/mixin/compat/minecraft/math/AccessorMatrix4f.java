package top.hendrixshen.magiclib.mixin.compat.minecraft.math;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.preprocess.api.DummyClass;

@Environment(EnvType.CLIENT)
@Mixin(DummyClass.class)
public interface AccessorMatrix4f {
}
