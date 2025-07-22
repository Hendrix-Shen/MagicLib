/*
 * This file is part of the TweakerMore project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2025  Fallen_Breath and contributors
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

package top.hendrixshen.magiclib.mixin.minecraft.render.compat.fabricrenderingapi;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.render.context.InWorldGuiDrawer;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/ddc655d68b6d5e34cce387863af1ffe79945befb/versions/1.21.6/src/main/java/me/fallenbreath/tweakermore/mixins/util/render/compat/fabricrenderingapi/SpecialGuiElementRegistryImplMixin.java">TweakerMore</a>.
 *
 * <p>
 * Fabric Rendering API does not expect that GuiRenderer was instantiated by a not-vanilla mod XD.
 * </p>
 *
 * <p>
 * See also: <a href="https://github.com/FabricMC/fabric/blob/05ccac950a2ea7ef8b4a4e08c955b96543f1ac59/fabric-rendering-v1/src/client/java/net/fabricmc/fabric/impl/client/rendering/SpecialGuiElementRegistryImpl.java#L40-L47">fabric api source code</a>
 * </p>
 *
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.21.6+        : subproject 1.21.8        &lt;--------</li>
 */
@Dependencies(require = @Dependency(value = "fabric-rendering-v1", versionPredicates = ">=0.127.0"))
@Pseudo
@Mixin(targets = "net.fabricmc.fabric.impl.client.rendering.SpecialGuiElementRegistryImpl", remap = false)
public abstract class SpecialGuiElementRegistryImpl {
    @Inject(method = "onReady", at = @At("HEAD"), cancellable = true)
    private static void cancelRegistrationHandlerForInWorldGuiDrawer(CallbackInfo ci) {
        if (InWorldGuiDrawer.isInitializing()) {
            ci.cancel();
        }
    }
}
