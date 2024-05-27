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

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.render.context.RenderContext;

import java.util.Objects;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/util/render/RenderUtil.java">TweakerMore</a>
 */
public class Scaler {
    private final double anchorX;
    private final double anchorY;
    private final double factor;

    private RenderContext context;

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
     * Pose stack of renderContext will be pushed
     */
    public void apply(RenderContext context) {
        this.context = context;
        this.context.pushPose();
        this.context.translate(-anchorX * factor, -anchorY * factor, 0);
        this.context.scale(factor, factor, 1);
        this.context.translate(anchorX / factor, anchorY / factor, 0);
    }

    /**
     * Pose stack of renderContext will be popped
     */
    public void restore() {
        if (this.context == null) {
            throw new RuntimeException("Scaler: Calling restore before calling apply");
        }

        this.context.popPose();
    }

    public RenderContext getRenderContext() {
        return Objects.requireNonNull(this.context);
    }
}
