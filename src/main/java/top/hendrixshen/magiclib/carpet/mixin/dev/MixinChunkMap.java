package top.hendrixshen.magiclib.carpet.mixin.dev;

import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;
import top.hendrixshen.magiclib.dependency.impl.MixinDependencyPredicates;
//#if MC > 11502 && MC < 11800
//$$ import com.google.common.collect.Iterables;
//$$ import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
//$$ import net.minecraft.server.level.ChunkHolder;
//$$ import org.spongepowered.asm.mixin.injection.At;
//$$ import org.spongepowered.asm.mixin.injection.Inject;
//$$ import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif

@Dependencies(
        and = {
                @Dependency("carpet"),
                @Dependency(value = "minecraft", versionPredicate = ">1.15.2"),
                @Dependency(value = "minecraft", versionPredicate = "<1.18")
        },
        predicate = MixinDependencyPredicates.DevMojangMixinPredicate.class
)
// Transforming after Carpet injection
@Mixin(value = ChunkMap.class, priority = 1001)
public class MixinChunkMap {
    //#if MC > 11502 && MC < 11800
    //$$ @Shadow
    //$$ private volatile Long2ObjectLinkedOpenHashMap<ChunkHolder> visibleChunkMap;
    //$$
    //$$ /**
    //$$  * Fix a stack overflow exception when you use Mojang Mappings in a development environment.
    //$$  * And since 1.18, carpet uses Mojang Mappings, this bug fixed itself.
    //$$  */
    //$$ @Inject(
    //$$         method = "getChunks",
    //$$         at = @At(
    //$$                 value = "HEAD"
    //$$         ),
    //$$         cancellable = true
    //$$ )
    //$$ private void getChunks(CallbackInfoReturnable<Iterable<ChunkHolder>> cir) {
    //$$     cir.setReturnValue(Iterables.unmodifiableIterable(this.visibleChunkMap.values()));
    //$$ }
    //#endif
}
