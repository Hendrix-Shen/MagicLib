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

package top.hendrixshen.magiclib.impl.render.context;

import lombok.Getter;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.state.GuiRenderState;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.fog.FogRenderer;

import top.hendrixshen.magiclib.api.fake.render.InWorldGuiRendererHook;
import top.hendrixshen.magiclib.util.function.MemoizedSupplier;
import top.hendrixshen.magiclib.util.minecraft.render.RenderUtil;

import java.util.List;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/073a2b6047084feedfe74c29274bcc3bdd750792/versions/1.21.8/src/main/java/me/fallenbreath/tweakermore/util/render/context/InWorldGuiDrawer.java">TweakerMore</a>.
 *
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project)</li>
 * <li>mc1.21.6+        : subproject 1.21.8        &lt;--------</li>
 */
public class InWorldGuiDrawer implements AutoCloseable {
    @Getter
    private static boolean initializing = false;
    private static final MemoizedSupplier<InWorldGuiDrawer> INSTANCE = new MemoizedSupplier<>(() -> {
        InWorldGuiDrawer.initializing = true;
        InWorldGuiDrawer inst = new InWorldGuiDrawer();
        InWorldGuiDrawer.initializing = false;
        return inst;
    });

    @Getter
    private final GuiGraphics guiGraphics;
    private final GuiRenderState guiState;
    private final GuiRenderer guiRenderer;
    private final FogRenderer fogRenderer;

    private InWorldGuiDrawer() {
        // reference: net.minecraft.client.renderer.GameRenderer#GameRenderer
        Minecraft mc = Minecraft.getInstance();
        MultiBufferSource.BufferSource immediate = RenderUtil.getBufferSource();
        this.guiState = new GuiRenderState();
        //#if MC >= 12111
        //$$ // TODO: check if mouseX,mouseY setting to 0,0 works
        //$$ this.guiGraphics = new GuiGraphics(mc, this.guiState, 0, 0);
        //#else
        this.guiGraphics = new GuiGraphics(mc, this.guiState);
        //#endif
        this.guiRenderer = new GuiRenderer(
                this.guiState,
                immediate,
                //#if MC >= 12109
                //$$ mc.gameRenderer.getSubmitNodeStorage(),
                //$$ mc.gameRenderer.getFeatureRenderDispatcher(),
                //#endif
                List.of()
        );
        ((InWorldGuiRendererHook) this.guiRenderer).magiclib$setInWorldGuiRender(true);
        this.fogRenderer = new FogRenderer();
    }

    public static InWorldGuiDrawer getInstance() {
        return InWorldGuiDrawer.INSTANCE.get();
    }

    public static void closeInstance() {
        if (InWorldGuiDrawer.INSTANCE.hasValue()) {
            InWorldGuiDrawer.INSTANCE.get().close();
        }
    }

    public void render() {
        RenderSystem.backupProjectionMatrix();
        this.guiRenderer.render(this.fogRenderer.getBuffer(FogRenderer.FogMode.NONE));
        RenderSystem.restoreProjectionMatrix();
        this.guiRenderer.incrementFrameNumber();
        this.fogRenderer.endFrame();
        this.guiState.reset();
    }

    @Override
    public void close() {
        this.guiRenderer.close();
        this.fogRenderer.close();
    }
}
