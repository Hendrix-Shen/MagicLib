package top.hendrixshen.magiclib.mixin.carpet.dev;

import com.google.common.collect.Iterables;
import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;
import top.hendrixshen.magiclib.dependency.impl.MixinDependencyPredicates;

/**
 * The implementation for mc [1.15.2, ~)
 * <p>
 * Fix a stack overflow exception when you use Mojang Mappings in a development environment.
 * And since 1.18, carpet uses Mojang Mappings, this bug fixed itself.
 */
@Dependencies(and = @Dependency("carpet"), predicate = MixinDependencyPredicates.DevMojangMixinPredicate.class)
@Mixin(value = ChunkMap.class, priority = 1001)
public class MixinChunkMap {
    @Shadow
    private volatile Long2ObjectLinkedOpenHashMap<ChunkHolder> visibleChunkMap;


    @Inject(
            method = "getChunks",
            at = @At(
                    value = "HEAD"
            ),
            cancellable = true
    )
    private void getChunks(@NotNull CallbackInfoReturnable<Iterable<ChunkHolder>> cir) {
        cir.setReturnValue(Iterables.unmodifiableIterable(this.visibleChunkMap.values()));
    }
}
