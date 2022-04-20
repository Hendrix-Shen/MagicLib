package com.mojang.blaze3d.systems;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.renderer.ShaderInstanceCompat;

import java.util.function.Supplier;

public class RenderSystem extends GlStateManager {
    public static void applyModelViewMatrix() {
    }

    public static void defaultBlendFunc() {
        blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
    }

    public static void setShader(Supplier<ShaderInstanceCompat> supplier) {
    }
}
