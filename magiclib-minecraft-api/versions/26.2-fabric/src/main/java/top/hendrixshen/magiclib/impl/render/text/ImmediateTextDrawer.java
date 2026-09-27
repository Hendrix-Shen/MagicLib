/*
 * This file is part of the TweakerMore project, licensed under the
 * GNU Lesser General Public License v3.0
 *
 * Copyright (C) 2026  Fallen_Breath and contributors
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

package top.hendrixshen.magiclib.impl.render.text;

import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.rendertype.RenderType;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Appends the glyphs of a {@code TextRenderer} to a {@link TextRenderBatch}.
 *
 * <p>
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/55abdaf4944cc0436fa5d94439953a8aef513b9f/versions/26.2/src/main/java/me/fallenbreath/tweakermore/util/render/ImmediateTextDrawer.java">TweakerMore</a>.
 *
 * <p>
 * When a batch is active, the glyphs become part of the shared batch and are submitted by the owner of that batch. When
 * no batch is active, this drawer owns a standalone batch which is submitted and released as soon as the drawer is
 * closed.
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc26.1: subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc26.2+        : subproject 26.2        &lt;--------</li>
 */
@ApiStatus.Internal
public class ImmediateTextDrawer implements Font.GlyphVisitor, AutoCloseable {
    private final Matrix4fc pose;
    private final Font.DisplayMode displayMode;
    private final int lightCoords;
    private final TextRenderBatch batch;
    private final boolean immediate;
    private final Map<RenderType, StagedVertexBuffer.Draw> draws = new LinkedHashMap<>();

    public ImmediateTextDrawer(Matrix4fc pose, Font.DisplayMode displayMode, int lightCoords) {
        this.pose = new Matrix4f(pose);
        this.displayMode = displayMode;
        this.lightCoords = lightCoords;
        TextRenderBatch activeBatch = TextRenderBatch.getActiveBatch();
        this.immediate = activeBatch == null;
        this.batch = this.immediate ? TextRenderBatch.createImmediate() : activeBatch;
    }

    public void append(Font.PreparedText preparedText) {
        preparedText.visit(this);
    }

    @Override
    public void acceptRenderable(TextRenderable renderable) {
        RenderType renderType = renderable.renderType(this.displayMode);
        StagedVertexBuffer.Draw draw = this.draws.get(renderType);

        if (draw == null) {
            // Only the first draw of this text group may merge into the previous draw entry.
            draw = this.batch.getOrCreateDraw(renderType, this.draws.isEmpty());
            this.draws.put(renderType, draw);
        }

        renderable.render(this.pose, this.batch.getStagedBuffer().getVertexBuilder(draw), this.lightCoords, false);
    }

    public void draw() {
        if (this.immediate) {
            this.batch.submit();
        }
    }

    @Override
    public void close() {
        if (this.immediate) {
            this.batch.submit();
            this.batch.close();
        }
    }
}
