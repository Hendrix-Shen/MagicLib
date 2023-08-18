package top.hendrixshen.magiclib.mixin.util.qol.auth;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.preprocess.api.DummyClass;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.impl.MixinDependencyPredicates;

@Dependencies(predicate = MixinDependencyPredicates.DevMixinPredicate.class)
@Environment(EnvType.CLIENT)
@Mixin(DummyClass.class)
public class MixinMinecraft {
}
