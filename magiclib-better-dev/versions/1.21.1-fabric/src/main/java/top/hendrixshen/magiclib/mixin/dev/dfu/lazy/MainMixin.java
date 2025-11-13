package top.hendrixshen.magiclib.mixin.dev.dfu.lazy;

import org.jetbrains.annotations.NotNull;

import com.mojang.datafixers.DSL;
import net.minecraft.client.main.Main;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.dev.MixinPredicates;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.21: subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.21.1+      : subproject 1.21.1        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Dependencies(require = @Dependency(dependencyType = DependencyType.PREDICATE, predicate = MixinPredicates.LazyDFUPredicate.class))
@Mixin(Main.class)
public abstract class MainMixin {
    @Redirect(
            method = "main",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/datafix/DataFixers;optimize(Ljava/util/Set;)Ljava/util/concurrent/CompletableFuture;"
            ),
            require = 0
    )
    private static @NotNull CompletableFuture<?> doNotBuild(Set<DSL.TypeReference> set) {
        return CompletableFuture.completedFuture(null);
    }
}
