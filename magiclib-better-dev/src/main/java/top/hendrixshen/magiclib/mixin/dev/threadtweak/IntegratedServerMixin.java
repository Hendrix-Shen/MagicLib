/*
 * MIT License
 *
 * Copyright 2022 Steven Cao
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

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

/**
 * Reference to <a href="https://github.com/UltimateBoomer/mc-smoothboot/blob/9a519ade89af24aa8b337dfed7d8eb8c0b62ec81/src/main/java/io/github/ultimateboomer/smoothboot/mixin/client/IntegratedServerMixin.java">SmoothBoot<a/>
 */
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
