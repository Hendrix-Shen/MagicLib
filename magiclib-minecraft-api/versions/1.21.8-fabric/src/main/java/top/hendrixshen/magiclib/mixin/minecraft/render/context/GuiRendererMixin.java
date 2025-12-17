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

package top.hendrixshen.magiclib.mixin.minecraft.render.context;

import org.joml.Matrix4fc;

import com.mojang.blaze3d.ProjectionType;
import com.mojang.blaze3d.buffers.GpuBufferSlice;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.render.GuiRenderer;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import top.hendrixshen.magiclib.api.fake.render.InWorldGuiRendererHook;
import top.hendrixshen.magiclib.libs.com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import top.hendrixshen.magiclib.libs.com.llamalad7.mixinextras.sugar.Local;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/70d455206b76922b274f36535f5ebdcec8faa7ac/versions/1.21.8/src/main/java/me/fallenbreath/tweakermore/mixins/util/render/GuiRendererMixin.java">TweakerMore</a>.
 *
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.21.6+        : subproject 1.21.8        &lt;--------</li>
 */
@Mixin(GuiRenderer.class)
public abstract class GuiRendererMixin implements InWorldGuiRendererHook {
    @Unique
    private boolean magiclib$inWorldGuiRender;

    @Override
    public void magiclib$setInWorldGuiRender(boolean flag) {
        this.magiclib$inWorldGuiRender = flag;
    }

    @WrapWithCondition(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/blaze3d/systems/RenderSystem;setProjectionMatrix(Lcom/mojang/blaze3d/buffers/GpuBufferSlice;Lcom/mojang/blaze3d/ProjectionType;)V")
    )
    private boolean skipSetProjectionMatrixForInWorldGuiRendering(GpuBufferSlice gpuBufferSlice, ProjectionType projectionType, @Local Window window) {
        return !this.magiclib$inWorldGuiRender;
    }

    @ModifyArg(
            method = "draw",
            at = @At(
                    value = "INVOKE",
                    //#if MC >= 12111
                    //$$ target = "Lnet/minecraft/client/renderer/DynamicUniforms;writeTransform(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"
                    //#else
                    target = "Lnet/minecraft/client/renderer/DynamicUniforms;writeTransform(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;F)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"
                    //#endif
            ),
            index = 0
    )
    private Matrix4fc restoreModelViewMatrix(Matrix4fc matrix4fc) {
        if (this.magiclib$inWorldGuiRender) {
            matrix4fc = RenderSystem.getModelViewMatrix();
        }

        return matrix4fc;
    }
}
