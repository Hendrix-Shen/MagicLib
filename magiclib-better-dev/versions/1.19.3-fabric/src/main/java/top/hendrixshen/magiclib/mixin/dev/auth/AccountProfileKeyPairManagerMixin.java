package top.hendrixshen.magiclib.mixin.dev.auth;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.AccountProfileKeyPairManager;
import net.minecraft.world.entity.player.ProfileKeyPair;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.api.preprocess.DummyClass;
import top.hendrixshen.magiclib.impl.dev.dfu.lazy.MixinPredicates;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@CompositeDependencies(
        @Dependencies(
                require = @Dependency(
                        dependencyType = DependencyType.PREDICATE,
                        predicate = MixinPredicates.PredicatePredicate.class
                )
        )
)
@Environment(EnvType.CLIENT)
@Mixin(AccountProfileKeyPairManager.class)
public class AccountProfileKeyPairManagerMixin {
    @Inject(
            method = "prepareKeyPair",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onPrepareKeyPair(@NotNull CallbackInfoReturnable<CompletableFuture<Optional<ProfileKeyPair>>> cir) {
        cir.setReturnValue(CompletableFuture.completedFuture(Optional.empty()));

    }
}
