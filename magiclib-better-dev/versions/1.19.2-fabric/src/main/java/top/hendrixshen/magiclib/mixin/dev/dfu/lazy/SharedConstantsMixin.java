package top.hendrixshen.magiclib.mixin.dev.dfu.lazy;

import net.minecraft.SharedConstants;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.dev.dfu.lazy.MixinPredicates;

@CompositeDependencies(
        @Dependencies(
                require = @Dependency(
                        dependencyType = DependencyType.PREDICATE,
                        predicate = MixinPredicates.LazyDFUPredicate.class
                )
        )
)
@Mixin(SharedConstants.class)
public class SharedConstantsMixin {
    @Inject(
            method = "enableDataFixerOptimizations",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private static void onEnableDataFixerOptimizations(@NotNull CallbackInfo ci) {
        ci.cancel();
    }
}
