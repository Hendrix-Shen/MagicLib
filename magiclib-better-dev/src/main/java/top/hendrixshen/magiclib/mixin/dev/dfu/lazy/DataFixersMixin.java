package top.hendrixshen.magiclib.mixin.dev.dfu.lazy;

import com.mojang.datafixers.DataFixerBuilder;
import net.minecraft.util.datafix.DataFixers;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.dev.MixinPredicates;
import top.hendrixshen.magiclib.impl.dev.dfu.lazy.LazyDataFixerBuilder;

@CompositeDependencies(
        @Dependencies(
                require = @Dependency(
                        dependencyType = DependencyType.PREDICATE,
                        predicate = MixinPredicates.LazyDFUPredicate.class
                )
        )
)
@Mixin(value = DataFixers.class, remap = false)
public class DataFixersMixin {
    @Redirect(
            method = "createFixerUpper",
            at = @At(
                    value = "NEW",
                    target = "Lcom/mojang/datafixers/DataFixerBuilder;"
            )
    )
    private static @NotNull DataFixerBuilder onCreateFixerUpper(int dataVersion) {
        return new LazyDataFixerBuilder(dataVersion);
    }
}
