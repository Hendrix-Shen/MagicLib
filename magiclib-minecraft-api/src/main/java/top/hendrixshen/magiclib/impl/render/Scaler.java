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

package top.hendrixshen.magiclib.impl.render;

import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 11904
//$$ import org.joml.Matrix3x2f;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.render.context.GuiRenderContext;
import top.hendrixshen.magiclib.api.render.context.LevelRenderContext;
import top.hendrixshen.magiclib.api.render.context.RenderContext;

import java.util.Objects;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/8af864d8c0070f3237736ea80f5050b726e367e3/src/main/java/me/fallenbreath/tweakermore/util/render/RenderUtils.java">TweakerMore</a>.
 */
public class Scaler {
    private final double anchorX;
    private final double anchorY;
    private final double factor;

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    private RenderContext context;
    private Runnable restoreFunc;

    public static @NotNull Scaler create(double anchorX, double anchorY, double factor) {
        return new Scaler(anchorX, anchorY, factor);
    }

    private Scaler(double anchorX, double anchorY, double factor) {
        this.anchorX = anchorX;
        this.anchorY = anchorY;

        if (factor <= 0) {
            throw new IllegalArgumentException("factor should be greater than 0, but " + factor + " found");
        }

        this.factor = factor;
    }

    /**
     * Pose stack of renderContext will be pushed.
     */
    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public void apply(RenderContext context) {
        this.context = context;
        context.pushMatrix();
        context.translate(-anchorX * factor, -anchorY * factor, 0);
        context.scale(factor, factor, 1);
        context.translate(anchorX / factor, anchorY / factor, 0);
        this.restoreFunc = context::popMatrix;
    }

    public void apply(LevelRenderContext context) {
        context.pushMatrix();
        context.translate(-anchorX * factor, -anchorY * factor, 0);
        context.scale(factor, factor, 1);
        context.translate(anchorX / factor, anchorY / factor, 0);
        this.restoreFunc = context::popMatrix;
    }

    public void apply(GuiRenderContext context) {
        context.pushMatrix();
        context.translate(-anchorX * factor, -anchorY * factor);
        context.scale(factor, factor);
        context.translate(anchorX / factor, anchorY / factor);
        this.restoreFunc = context::popMatrix;
    }

    //#if MC >= 11904
    //$$ public void apply(Matrix3x2f matrix) {
    //$$     matrix.translate((float) (-anchorX * factor), (float) (-anchorY * factor));
    //$$     matrix.scale((float) factor, (float) factor);
    //$$     matrix.translate((float) (anchorX / factor), (float) (anchorY / factor));
    //$$ }
    //#endif

    /**
     * Pose stack of renderContext will be popped.
     */
    public void restore() {
        if (this.restoreFunc == null) {
            throw new IllegalStateException("Scaler: Calling restore before calling apply");
        }

        this.restoreFunc.run();
        this.restoreFunc = null;
    }

    @Deprecated
    @ApiStatus.ScheduledForRemoval
    public RenderContext getRenderContext() {
        return Objects.requireNonNull(this.context);
    }
}
