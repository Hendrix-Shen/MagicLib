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

package top.hendrixshen.magiclib.mixin.dev.auth;

import org.jetbrains.annotations.NotNull;

import net.minecraft.client.multiplayer.AccountProfileKeyPairManager;
import net.minecraft.world.entity.player.ProfileKeyPair;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.dev.MixinPredicates;

import java.util.Optional;
import java.util.concurrent.CompletableFuture;

/**
 * Reference to <a href="https://github.com/magistermaks/mod-fungible/blob/9cd81f1d8ebcef43cff3df279aaef9bb68950e7c/src/main/java/net/darktree/fungible/mixin/auth/ProfileKeysImplMixin.java">mod-fungible</a>.
 *
 * <li>mc1.14 ~ mc1.19.2: subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.19.3+        : subproject 1.19.3        &lt;--------</li>
 */
@Dependencies(require = @Dependency(dependencyType = DependencyType.PREDICATE, predicate = MixinPredicates.AuthEmptyKeyPredicate.class))
@Mixin(AccountProfileKeyPairManager.class)
public abstract class AccountProfileKeyPairManagerMixin {
    @Inject(method = "prepareKeyPair", at = @At("HEAD"), cancellable = true)
    private void onPrepareKeyPair(@NotNull CallbackInfoReturnable<CompletableFuture<Optional<ProfileKeyPair>>> cir) {
        cir.setReturnValue(CompletableFuture.completedFuture(Optional.empty()));
    }
}
