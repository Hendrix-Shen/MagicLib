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
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Matrix4fStack;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 26.3
//$$ import com.mojang.blaze3d.pipeline.RenderTarget;
//$$ import com.mojang.renderpearl.api.commands.CommandEncoder;
//$$ import com.mojang.renderpearl.api.commands.RenderPass;
//$$ import net.minecraft.client.Minecraft;
//#endif
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.renderer.StagedVertexBuffer;
import net.minecraft.client.renderer.rendertype.RenderType;
// CHECKSTYLE.ON: ImportOrder

import java.util.ArrayList;
import java.util.List;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 26.3
//$$ import java.util.Optional;
//$$ import java.util.OptionalDouble;
//#endif
// CHECKSTYLE.ON: ImportOrder

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/55abdaf4944cc0436fa5d94439953a8aef513b9f/versions/26.2/src/main/java/me/fallenbreath/tweakermore/util/render/TextRenderBatch.java">TweakerMore</a>.
 *
 * <p>
 * Batches the {@code TextRenderer} geometry of a frame into a single {@link StagedVertexBuffer} and submits it once.
 *
 * <p>
 * Minecraft 26.2 introduced an experimental Vulkan backend which owns no OpenGL context at all, so the immediate
 * graphics state used by {@code RenderGlobal} is unavailable there. The build, upload and execute flow of this class
 * follows the Minecraft 26.2 {@code TextFeatureRenderer}, {@code RenderTypeFeatureRenderer} and
 * {@code StagedVertexBuffer}: glyphs are appended to a staging buffer in the order they were submitted, and the whole
 * batch is uploaded once and executed with the render types of the glyphs.
 *
 * <p>
 * Unlike vanilla feature submission, this batch runs from MagicLib's late level-render hook, after the feature frame
 * has been prepared and after the level frame graph finished executing, so it never opens a render pass while another
 * one is still open. The model view matrix of the moment of submission is restored while the batch is submitted, since a
 * barrier such as the GUI blur effect may submit it while another renderer has a local model view transform applied.
 *
 * <p>
 * Batch rendering is an internal implementation detail of MagicLib. It is not a public API and may change at any time.
 *
 * <p>
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc26.1: subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc26.2+        : subproject 26.2        &lt;--------</li>
 */
@ApiStatus.Internal
public final class TextRenderBatch implements AutoCloseable {
    private static final int INITIAL_CAPACITY = 65536;

    private static @Nullable TextRenderBatch sharedBatch;
    private static @Nullable TextRenderBatch activeBatch;
    private static int scopeDepth;

    // A GUI barrier may flush while another renderer has a local model view transform applied, so the model view matrix
    // of the moment of submission is restored while the batch is submitted.
    private final Matrix4f baseModelView = new Matrix4f();
    private final StagedVertexBuffer stagedBuffer = new StagedVertexBuffer(() -> "MagicLib TextRenderer", INITIAL_CAPACITY);
    private final List<DrawEntry> draws = new ArrayList<>();
    private @Nullable DrawEntry lastDraw;
    private boolean uploaded;

    /**
     * Starts collecting text into the shared batch.
     *
     * <p>
     * Starting a batch while one is already collecting only enters the existing scope, so a renderer may wrap its own
     * drawing in a batch scope without having to know whether an outer scope already exists.
     */
    public static void beginBatch() {
        RenderSystem.assertOnRenderThread();

        if (TextRenderBatch.activeBatch != null) {
            TextRenderBatch.scopeDepth++;
            return;
        }

        if (TextRenderBatch.sharedBatch == null) {
            TextRenderBatch.sharedBatch = new TextRenderBatch();
        }

        TextRenderBatch.activeBatch = TextRenderBatch.sharedBatch;
        TextRenderBatch.scopeDepth = 1;
    }

    /**
     * Submits the shared batch and ends the collecting scope.
     *
     * @throws IllegalStateException if no batch has been started.
     */
    public static void endBatch() {
        RenderSystem.assertOnRenderThread();
        TextRenderBatch batch = TextRenderBatch.activeBatch;

        if (batch == null || TextRenderBatch.scopeDepth <= 0) {
            throw new IllegalStateException("Text render batch has not started");
        }

        if (--TextRenderBatch.scopeDepth > 0) {
            return;
        }

        TextRenderBatch.activeBatch = null;

        try {
            batch.submit();
        } finally {
            batch.stagedBuffer.endFrame();
        }
    }

    /**
     * Submits the shared batch without ending the collecting scope, so the batch keeps collecting afterwards.
     */
    public static void flushActiveBatch() {
        RenderSystem.assertOnRenderThread();

        if (TextRenderBatch.activeBatch != null) {
            TextRenderBatch.activeBatch.submit();
        }
    }

    /**
     * Submits and releases the shared batch.
     */
    public static void closeSharedBatch() {
        RenderSystem.assertOnRenderThread();

        if (TextRenderBatch.activeBatch != null) {
            try {
                TextRenderBatch.activeBatch.submit();
            } finally {
                TextRenderBatch.activeBatch.stagedBuffer.endFrame();
                TextRenderBatch.activeBatch = null;
                TextRenderBatch.scopeDepth = 0;
            }
        }

        if (TextRenderBatch.sharedBatch != null) {
            TextRenderBatch.sharedBatch.close();
            TextRenderBatch.sharedBatch = null;
        }
    }

    /**
     * Gets the batch which is currently collecting, if any.
     *
     * @return the active batch, or {@code null} when no batch has been started.
     */
    public static @Nullable TextRenderBatch getActiveBatch() {
        return TextRenderBatch.activeBatch;
    }

    /**
     * Creates a standalone batch which owns its staging buffer.
     *
     * @return a new standalone batch.
     */
    public static TextRenderBatch createImmediate() {
        return new TextRenderBatch();
    }

    /**
     * Gets the staging buffer of this batch.
     *
     * @return the staging buffer of this batch.
     */
    public StagedVertexBuffer getStagedBuffer() {
        return this.stagedBuffer;
    }

    /**
     * Gets or creates the draw entry of the given render type.
     *
     * @param renderType               the render type to draw with.
     * @param canMergeWithPreviousDraw whether this draw may be merged into the previous draw entry when it uses the
     *                                 same render type. Only the first draw of a text group may merge across its
     *                                 ordering boundary, otherwise the submission order would be violated.
     * @return the draw to append the vertices of this render type to.
     */
    public StagedVertexBuffer.Draw getOrCreateDraw(RenderType renderType, boolean canMergeWithPreviousDraw) {
        if (this.uploaded) {
            throw new IllegalStateException("Cannot append a draw to an uploaded batch");
        }

        if (canMergeWithPreviousDraw
                && this.lastDraw != null
                && this.lastDraw.renderType() == renderType
                && renderType.canConsolidateConsecutiveGeometry()) {
            return this.lastDraw.draw();
        }

        StagedVertexBuffer.Draw draw = this.stagedBuffer.appendDraw(
                renderType.format(),
                renderType.primitiveTopology(),
                renderType.sortOnUpload() ? RenderSystem.getProjectionType().vertexSorting() : null
        );
        this.lastDraw = new DrawEntry(renderType, draw);
        this.draws.add(this.lastDraw);
        return draw;
    }

    /**
     * Captures the current model view matrix as the model view matrix of this batch.
     */
    public void captureBaseModelView() {
        this.baseModelView.set(RenderSystem.getModelViewStack());
    }

    /**
     * Submits the collected draws, if any.
     *
     * <p>
     * This method is a no-op when nothing has been collected, and it resets the batch so it can collect again. It must
     * only be called while no render pass is open.
     */
    public void submit() {
        if (this.draws.isEmpty()) {
            return;
        }

        RenderSystem.assertOnRenderThread();
        this.captureBaseModelView();
        Matrix4fStack modelViewStack = RenderSystem.getModelViewStack();
        modelViewStack.pushMatrix();
        modelViewStack.set(this.baseModelView);

        try {
            // Follow vanilla's staged flow: upload once, execute the ordered draws, then end the draw.
            this.stagedBuffer.upload();
            this.uploaded = true;
            this.execute();
        } finally {
            try {
                this.stagedBuffer.endDraw();
                this.uploaded = false;
                this.draws.clear();
                this.lastDraw = null;
            } finally {
                modelViewStack.popMatrix();
            }
        }
    }

    @Override
    public void close() {
        this.stagedBuffer.close();
    }

    private void execute() {
        //#if MC >= 26.3
        //$$ RenderTarget renderTarget = Minecraft.getInstance().gameRenderer.mainRenderTarget();
        //$$ CommandEncoder commandEncoder = RenderSystem.getDevice().createCommandEncoder();
        //$$
        //$$ try (RenderPass renderPass = commandEncoder.createRenderPass(
        //$$         () -> "MagicLib TextRenderer",
        //$$         renderTarget.getColorTextureView(),
        //$$         Optional.empty(),
        //$$         renderTarget.hasDepth() ? renderTarget.getDepthTextureView() : null,
        //$$         OptionalDouble.empty())) {
        //$$     for (DrawEntry entry : this.draws) {
        //$$         StagedVertexBuffer.ExecuteInfo executeInfo = this.stagedBuffer.getExecuteInfo(entry.draw());
        //$$
        //$$         if (executeInfo != null) {
        //$$             entry.renderType().prepare().drawFromBuffer(executeInfo, renderPass);
        //$$         }
        //$$     }
        //$$ }
        //#else
        for (DrawEntry entry : this.draws) {
            StagedVertexBuffer.ExecuteInfo executeInfo = this.stagedBuffer.getExecuteInfo(entry.draw());

            if (executeInfo != null) {
                // Preparing the render type bakes the model view matrix and the glyph texture of this very moment into
                // a drawable pipeline state, which is what replaces the immediate graphics state on non-OpenGL
                // backends.
                entry.renderType().prepare().drawFromBuffer(executeInfo);
            }
        }
        //#endif
    }

    private record DrawEntry(RenderType renderType, StagedVertexBuffer.Draw draw) {
    }
}
