package top.hendrixshen.magiclib.impl.mixin.common.carpet;

import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.dependency.Predicates;
import top.hendrixshen.magiclib.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.annotation.Dependency;

@Dependencies(and = {
        @Dependency(value = "minecraft", versionPredicate = ">1.15.2"),
        @Dependency(value = "minecraft", versionPredicate = "<1.18")},
        predicate = Predicates.DevMojangMixinPredicate.class)
@Mixin(value = ChunkMap.class)
public class MixinChunkMap {
}
