package top.hendrixshen.magiclib.mixin.carpet.compat.carpettisaddition;

import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.preprocess.api.DummyClass;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;

@Dependencies(
        and = {
                @Dependency("carpet"),
                @Dependency(value = "carpet-tis-addition", versionPredicate = ">=1.38")
        }
)
@Mixin(DummyClass.class)
public class MixinCarpetRuleRegistrar {
}
