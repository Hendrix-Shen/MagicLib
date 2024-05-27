/*
 * Copyright (c) 2022 magistermaks
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * and a copy of GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>
 */

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
import top.hendrixshen.magiclib.impl.dev.MixinPredicates;

/**
 * Reference to <a href="https://github.com/magistermaks/mod-fungible/blob/9cd81f1d8ebcef43cff3df279aaef9bb68950e7c/src/main/java/net/darktree/fungible/mixin/chunk_loading/MinecraftServerMixin.java">Fungible<a/>
 */
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
