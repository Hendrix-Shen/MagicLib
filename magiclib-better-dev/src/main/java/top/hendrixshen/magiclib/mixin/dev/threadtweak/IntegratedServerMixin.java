package top.hendrixshen.magiclib.mixin.dev.threadtweak;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.server.IntegratedServer;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.dev.MixinPredicates;
import top.hendrixshen.magiclib.impl.dev.threadtweak.ThreadTweaker;

//#if MC > 11802
//$$ import net.minecraft.server.Services;
//#else
import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import net.minecraft.server.players.GameProfileCache;
//#endif

//#if MC > 11502
//#if MC > 11701
//$$ import net.minecraft.server.WorldStem;
//#else
import net.minecraft.core.RegistryAccess;
import net.minecraft.server.ServerResources;
import net.minecraft.world.level.storage.WorldData;
//#endif
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.world.level.storage.LevelStorageSource;
//#else
//$$ import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
//$$ import net.minecraft.world.level.LevelSettings;
//#endif

@CompositeDependencies(
        @Dependencies(
                require = @Dependency(
                        dependencyType = DependencyType.PREDICATE,
                        predicate = MixinPredicates.TheadTweakPredicate.class
                )
        )
)
@Environment(EnvType.CLIENT)
@Mixin(IntegratedServer.class)
public class IntegratedServerMixin {
    @Inject(
            method = "<init>",
            at = @At(
                    value = "RETURN"
            ),
            remap = false
    )
    private void onInstanceInit(
            //#if MC > 11502
            Thread thread,
            //#endif
            Minecraft minecraft,
            //#if MC > 11502
            //#if MC < 11802
            RegistryAccess.RegistryHolder registryHolder,
            //#endif
            LevelStorageSource.LevelStorageAccess levelStorageAccess,
            PackRepository packRepository,
            //#if MC > 11701
            //$$ WorldStem worldStem,
            //#else
            ServerResources serverResources,
            WorldData worldData,
            //#endif
            //#else
            //$$ String levelIdName,
            //$$ String levelName,
            //$$ LevelSettings levelSettings,
            //$$ YggdrasilAuthenticationService yggdrasilAuthenticationService,
            //#endif
            //#if MC > 11802
            //$$ Services services,
            //#else
            MinecraftSessionService minecraftSessionService,
            GameProfileRepository gameProfileRepository,
            GameProfileCache gameProfileCache,
            //#endif
            ChunkProgressListenerFactory chunkProgressListenerFactory,
            CallbackInfo ci
    ) {
        Thread.currentThread().setPriority(ThreadTweaker.getGamePriority());
    }
}
