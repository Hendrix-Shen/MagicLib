package top.hendrixshen.magiclib.mixin.dev.chunk;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.progress.ChunkProgressListener;
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
                        predicate = MixinPredicates.ChunkPredicate.class
                )
        )
)
@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(
            method = "prepareLevels",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void onPrepareLevels(ChunkProgressListener chunkProgressListener, @NotNull CallbackInfo ci) {
        ci.cancel();
    }
}
