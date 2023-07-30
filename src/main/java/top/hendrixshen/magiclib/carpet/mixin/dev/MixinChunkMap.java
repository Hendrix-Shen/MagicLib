package top.hendrixshen.magiclib.carpet.mixin.dev;

import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.preprocess.api.DummyClass;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;
import top.hendrixshen.magiclib.dependency.impl.MixinDependencyPredicates;

@Dependencies(and = @Dependency("carpet"), predicate = MixinDependencyPredicates.DevMojangMixinPredicate.class)
@Mixin(value = DummyClass.class, priority = 1001)
public class MixinChunkMap {
}
