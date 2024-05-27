/*
 * This file is part of the TweakerMore project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2023  Fallen_Breath and contributors
 *
 * TweakerMore is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * TweakerMore is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with TweakerMore.  If not, see <https://www.gnu.org/licenses/>.
 */

package top.hendrixshen.magiclib.mixin.dev.auth;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Coerce;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.dev.MixinPredicates;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/versions/1.16.5/src/main/java/me/fallenbreath/tweakermore/mixins/util/qol/MinecraftClientMixin.java">TweakerMore<a/>
 */
@CompositeDependencies(
        @Dependencies(
                require = @Dependency(
                        dependencyType = DependencyType.PREDICATE,
                        predicate = MixinPredicates.AuthVerifyPredicate.class
                )
        )
)
@Environment(EnvType.CLIENT)
@Mixin(Minecraft.class)
public class MinecraftMixin {
    @Redirect(
            //#if MC > 11701
            //$$ method = "createUserApiService",
            //#else
            method = "createSocialInteractions",
            //#endif
            at = @At(
                    value = "INVOKE",
                    //#if MC > 11701
                    //$$ target = "Lorg/slf4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V",
                    //#else
                    target = "Lorg/apache/logging/log4j/Logger;error(Ljava/lang/String;Ljava/lang/Throwable;)V",
                    //#endif
                    remap = false
            ),
            require = 0
    )
    private void dontThrowAuthFailureInDevEnv(@Coerce Object logger, String s, Throwable throwable) {
        // NO-OP
    }
}
