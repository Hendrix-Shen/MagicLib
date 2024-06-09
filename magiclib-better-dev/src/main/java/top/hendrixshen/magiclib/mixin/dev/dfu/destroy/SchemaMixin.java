package top.hendrixshen.magiclib.mixin.dev.dfu.destroy;

import com.mojang.datafixers.DSL;
import com.mojang.datafixers.schemas.Schema;
import com.mojang.datafixers.types.Type;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.dev.MixinPredicates;

@Dependencies(require = @Dependency(dependencyType = DependencyType.PREDICATE, predicate = MixinPredicates.DestroyDFUPredicate.class))
@Mixin(value = Schema.class, remap = false)
public class SchemaMixin {
    @Inject(
            method = "getType",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onGetType(DSL.TypeReference type, @NotNull CallbackInfoReturnable<Type<?>> cir) {
        cir.setReturnValue(null);
    }

    @Inject(
            method = "getTypeRaw",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onGetTypeRaw(DSL.TypeReference type, @NotNull CallbackInfoReturnable<Type<?>> cir) {
        cir.setReturnValue(null);
    }

    @Inject(
            method = "getChoiceType",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onGetChoiceType(DSL.TypeReference type, String choiceName,
                                 @NotNull CallbackInfoReturnable<Type<?>> cir) {
        cir.setReturnValue(null);
    }
}
