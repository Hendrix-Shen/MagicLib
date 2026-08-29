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

import com.google.common.collect.Maps;
import org.jetbrains.annotations.ApiStatus;
import org.joml.Matrix4f;
import org.joml.Matrix4fc;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.font.TextRenderable;
import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.StagedVertexBuffer.Draw;
import net.minecraft.client.renderer.rendertype.RenderType;

import java.util.Map;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/7a0d5d807d598418d2e97ee3fc97a252f38e5d6b/versions/26.2/src/main/java/me/fallenbreath/tweakermore/util/render/ImmediateTextDrawer.java">TweakerMore</a>.
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc26.1: subproject 1.16.5 (main project) [dummy]        &lt;--------</li>
 * <li>mc26.2+        : subproject 26.2</li>
 */
@ApiStatus.Internal
public class ImmediateTextDrawer implements Font.GlyphVisitor, AutoCloseable {
    private static final Matrix4fc IDENTITY_MATRIX = new Matrix4f();

    private final Font.DisplayMode displayMode;
    private final int lightCoords;
    private final StagedVertexBuffer stagedBuffer = new StagedVertexBuffer(() -> "MagicLib TextRenderer", 65536);
    private final Map<RenderType, Draw> draws = Maps.newLinkedHashMap();

    public ImmediateTextDrawer(Font.DisplayMode displayMode, int lightCoords) {
        this.displayMode = displayMode;
        this.lightCoords = lightCoords;
    }

    public void append(Font.PreparedText preparedText) {
        preparedText.visit(this);
    }

    @Override
    public void acceptRenderable(TextRenderable renderable) {
        RenderType renderType = renderable.renderType(this.displayMode);
        StagedVertexBuffer.Draw draw = this.draws.computeIfAbsent(renderType, this::createDraw);
        renderable.render(ImmediateTextDrawer.IDENTITY_MATRIX, this.stagedBuffer.getVertexBuilder(draw), this.lightCoords, false);
    }

    public void draw() {
        if (this.draws.isEmpty()) {
            return;
        }

        this.stagedBuffer.upload();
        this.draws.forEach((renderType, draw) -> {
            StagedVertexBuffer.ExecuteInfo executeInfo = this.stagedBuffer.getExecuteInfo(draw);
            if (executeInfo != null) {
                renderType.prepare().drawFromBuffer(executeInfo);
            }
        });
        this.stagedBuffer.endDraw();
    }

    @Override
    public void close() {
        this.stagedBuffer.close();
    }

    private StagedVertexBuffer.Draw createDraw(RenderType renderType) {
        return this.stagedBuffer.appendDraw(
                renderType.format(),
                renderType.primitiveTopology(),
                renderType.sortOnUpload() ? RenderSystem.getProjectionType().vertexSorting() : null
        );
    }
}
