package top.hendrixshen.magiclib.mixin.dev.dfu.destroy;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.DataFixerUpper;
import com.mojang.serialization.Dynamic;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.dev.MixinPredicates;

@CompositeDependencies(
        @Dependencies(
                require = @Dependency(
                        dependencyType = DependencyType.PREDICATE,
                        predicate = MixinPredicates.DestroyDFUPredicate.class
                )
        )
)
@Mixin(value = DataFixerUpper.class, remap = false)
public class DataFixerUpperMixin {
    @Inject(
            method = "update",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private <T> void onUpdate(DSL.TypeReference type, Dynamic<T> input, int version,
                              int newVersion, @NotNull CallbackInfoReturnable<Dynamic<T>> cir) {
        cir.setReturnValue(input);
    }
}
