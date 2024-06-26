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

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.render.context.RenderContext;
import top.hendrixshen.magiclib.impl.render.context.RenderGlobal;
import top.hendrixshen.magiclib.util.minecraft.PositionUtil;
import top.hendrixshen.magiclib.util.minecraft.render.RenderUtil;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

//#if MC >= 12100
//$$ import top.hendrixshen.magiclib.mixin.minecraft.accessor.TesselatorAccessor;
//#endif

//#if MC > 12005 || MC < 11700 && MC > 11502
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

//#if MC > 11903
//$$ import net.minecraft.client.gui.Font;
//#endif

//#if MC > 11605
//$$ import com.mojang.blaze3d.systems.RenderSystem;
//#endif

//#if MC > 11502
import net.minecraft.util.FormattedCharSequence;
import top.hendrixshen.magiclib.util.minecraft.render.TextRenderUtil;
//#endif

//#if MC > 11404
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.math.Matrix4f;
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.MultiBufferSource;
//#endif

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore/blob/10e1a937aadcefb1f2d9d9bab8badc873d4a5b3d/src/main/java/me/fallenbreath/tweakermore/util/render/TextRenderer.java">TweakerMore</a>
 */
public class TextRenderer {
    public static final double DEFAULT_FONT_SCALE = 0.025;

    private final List<TextHolder> lines;
    @Getter
    private Vec3 pos;
    private double shiftX;
    private double shiftY;
    private double fontScale;
    private double lineHeightRatio;
    private int color;
    private int backgroundColor;
    private boolean shadow;
    private boolean seeThrough;
    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;

    public static @NotNull TextRenderer create() {
        return new TextRenderer();
    }

    private TextRenderer() {
        this.lines = Lists.newArrayList();
        this.shiftX = this.shiftY = 0.0;
        this.fontScale = TextRenderer.DEFAULT_FONT_SCALE;
        this.lineHeightRatio = 1.0 * RenderUtil.TEXT_LINE_HEIGHT / RenderUtil.TEXT_HEIGHT;
        this.color = 0xFFFFFFFF;
        this.backgroundColor = 0x00000000;
        this.shadow = false;
        this.seeThrough = false;
        this.horizontalAlignment = HorizontalAlignment.DEFAULT;
        this.verticalAlignment = VerticalAlignment.DEFAULT;
    }

    /**
     * Draw given lines with centered format.
     * <p>
     * Reference: {@link net.minecraft.client.renderer.debug.DebugRenderer#renderFloatingText(String, double,
     * double, double, int, float, boolean, float, boolean)}
     * <p>
     * Note:
     * - shadow=true + seeThrough=false might result in weird rendering.
     */
    @SuppressWarnings("UnnecessaryLocalVariable")
    public void render() {
        if (this.lines.isEmpty()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        RenderContext context = RenderContext.of(
                //#if MC > 12004
                //$$ new PoseStack()
                //#elseif MC > 11605
                //$$ RenderSystem.getModelViewStack()
                //#elseif MC > 11502
                new PoseStack()
                //#endif
        );

        CameraPositionTransformer positionTransformer = CameraPositionTransformer.create(this.pos);
        positionTransformer.apply(context);
        context.scale(-this.fontScale, -this.fontScale, this.fontScale);
        //#if MC < 11700
        RenderGlobal.disableLighting();
        //#endif

        if (this.seeThrough) {
            RenderGlobal.disableDepthTest();
        } else {
            RenderGlobal.enableDepthTest();
        }

        //#if MC < 11904
        RenderGlobal.enableTexture();
        //#endif
        RenderGlobal.depthMask(true);
        int lineNum = this.lines.size();
        double maxTextWidth = this.lines.stream().mapToInt(TextHolder::getWidth).max().orElse(0);
        double totalTextWidth = maxTextWidth;
        double totalTextHeight = RenderUtil.TEXT_HEIGHT * lineNum + (this.lineHeightRatio - 1) * (lineNum - 1);
        context.translate(this.horizontalAlignment.getTranslateX(totalTextWidth),
                this.verticalAlignment.getTranslateY(totalTextHeight), 0);
        context.translate(this.shiftX, this.shiftY, 0);
        //#if MC > 11605
        //$$ RenderSystem.applyModelViewMatrix();
        //#else
        RenderGlobal.enableAlphaTest();
        //#endif
        // Enable transparent-able text rendering.
        RenderGlobal.enableBlend();
        RenderGlobal.blendFunc(
                //#if MC > 11404
                GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
                //#else
                //$$ GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA
                //#endif
        );

        for (int i = 0; i < lineNum; i++) {
            TextHolder holder = this.lines.get(i);
            float textX = (float) this.horizontalAlignment.getTextX(maxTextWidth, holder.getWidth());
            float textY = (float) (getLineHeight() * i);
            //#if MC > 11404
            int backgroundColor = this.backgroundColor;

            while (true) {
                MultiBufferSource.BufferSource immediate = MultiBufferSource.immediate(
                        //#if MC >= 12100
                        //$$ ((TesselatorAccessor) Tesselator.getInstance()).magiclib$getBuffer());
                        //#else
                        Tesselator.getInstance().getBuilder());
                        //#endif
                //#if MC > 12004
                //$$ Matrix4f matrix4f = context.getPoseStack().last().pose();
                //#else
                Matrix4f matrix4f = Transformation.identity().getMatrix();
                //#endif
                mc.font.drawInBatch(
                        holder.text,
                        textX,
                        textY,
                        this.color,
                        this.shadow,
                        matrix4f,
                        immediate,
                        //#if MC > 11903
                        //$$ this.seeThrough ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL,
                        //#else
                        this.seeThrough,
                        //#endif
                        backgroundColor,
                        0xF000F0
                );
                immediate.endBatch();

                // Draw twice when having background.
                if (backgroundColor == 0) {
                    break;
                } else {
                    backgroundColor = 0;
                }
            }
            //#else
            //$$ if (this.shadow) {
            //$$ 	mc.font.drawShadow(holder.text, textX, textY, this.color);
            //$$ } else {
            //$$ 	mc.font.draw(holder.text, textX, textY, this.color);
            //$$ }
            //#endif
        }

        //#if MC < 11600
        //$$ RenderGlobal.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        //#endif
        //TODO check color4f, see if it can replace blendFunc
        //#if MC < 11904
        RenderGlobal.enableDepthTest();
        //#endif
        positionTransformer.restore();
        //#if MC > 11605
        //$$ RenderSystem.applyModelViewMatrix();
        //#endif
    }

    private TextRenderer addLines(TextHolder... lines) {
        Collections.addAll(this.lines, lines);
        return this;
    }

    private TextRenderer setLines(TextHolder... lines) {
        this.lines.clear();
        this.addLines(lines);
        return this;
    }

    //#if MC > 11600
    public TextRenderer text(FormattedCharSequence text) {
        return this.setLines(TextHolder.of(text));
    }
    //#endif

    public TextRenderer text(String text) {
        return this.setLines(TextHolder.of(text));
    }

    public TextRenderer text(Component text) {
        return this.setLines(TextHolder.of(text));
    }

    //#if MC > 11502
    public TextRenderer addLine(FormattedCharSequence text) {
        return this.addLines(TextHolder.of(text));
    }
    //#endif

    public TextRenderer addLine(String text) {
        return this.addLines(TextHolder.of(text));
    }

    public TextRenderer addLine(Component text) {
        return this.addLines(TextHolder.of(text));
    }

    public TextRenderer lineHeightRatio(double lineHeightRatio) {
        this.lineHeightRatio = lineHeightRatio;
        return this;
    }

    public TextRenderer at(Vec3 vec3) {
        this.pos = vec3;
        return this;
    }

    public TextRenderer at(double x, double y, double z) {
        return this.at(new Vec3(x, y, z));
    }

    public TextRenderer atCenter(BlockPos blockPos) {
        return this.at(PositionUtil.centerOf(blockPos));
    }

    /**
     * Shift the text in the render length unit.
     */
    public TextRenderer shift(double x, double y) {
        this.shiftX = x;
        this.shiftY = y;
        return this;
    }

    public TextRenderer fontScale(double fontScale) {
        this.fontScale = fontScale;
        return this;
    }

    /**
     * @param color the text color in the 0xAARRGGBB format.
     */
    public TextRenderer color(int color) {
        this.color = color;
        return this;
    }

    /**
     * @param backgroundColor the background color in the 0xAARRGGBB format.
     */
    public TextRenderer bgColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    /**
     * @param color           the text color in the 0xAARRGGBB format.
     * @param backgroundColor the background color in the 0xAARRGGBB format.
     */
    public TextRenderer color(int color, int backgroundColor) {
        this.color(color);
        this.bgColor(backgroundColor);
        return this;
    }

    public TextRenderer shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public TextRenderer shadow() {
        return this.shadow(true);
    }

    public TextRenderer seeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
        return this;
    }

    public TextRenderer seeThrough() {
        return this.seeThrough(true);
    }

    public TextRenderer align(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
        return this;
    }

    public TextRenderer align(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
        return this;
    }

    public double getLineHeight() {
        return RenderUtil.TEXT_HEIGHT * this.lineHeightRatio;
    }

    private static class TextHolder {
        //#if MC > 11502
        public final FormattedCharSequence text;
        //#else
        //$$ public final String text;
        //#endif

        private TextHolder(
                //#if MC > 11502
                FormattedCharSequence text
                //#else
                //$$ String text
                //#endif
        ) {
            this.text = text;
        }

        public static @NotNull TextHolder of(
                //#if MC > 11600
                FormattedCharSequence text
                //#else
                //$$ String text
                //#endif
        ) {
            return new TextHolder(text);
        }

        //#if MC > 11502
        @Contract("_ -> new")
        public static @NotNull TextHolder of(String text) {
            return TextHolder.of(TextRenderUtil.string2formattedCharSequence(text));
        }
        //#endif

        public static @NotNull TextHolder of(@NotNull Component text) {
            return new TextHolder(
                    //#if MC > 11502
                    text.getVisualOrderText()
                    //#else
                    //$$ text.getColoredString()
                    //#endif
            );
        }

        public int getWidth() {
            return RenderUtil.getRenderWidth(this.text);
        }
    }

    public enum HorizontalAlignment {
        LEFT(w -> 0.0, (w, tw) -> 0.0),                   // [-x]  ^Text  [+x]
        RIGHT(w -> -w, (w, tw) -> w - tw),                // [-x]  Text^  [+x]
        CENTER(w -> -0.5 * w, (w, tw) -> 0.5 * (w - tw)); // [-x]  Te^xt  [+x]

        public static final HorizontalAlignment DEFAULT = HorizontalAlignment.CENTER;

        private final Function<Double, Double> trMapper;
        private final BiFunction<Double, Double, Double> posMapper;

        HorizontalAlignment(Function<Double, Double> trMapper, BiFunction<Double, Double, Double> posMapper) {
            this.trMapper = trMapper;
            this.posMapper = posMapper;
        }

        public double getTranslateX(double width) {
            return this.trMapper.apply(width);
        }

        public double getTextX(double width, double textWidth) {
            return this.posMapper.apply(width, textWidth);
        }
    }

    public enum VerticalAlignment {
        TOP(h -> 0.0),         // [-y]  ^Text  [+y]
        BOTTOM(h -> -h),       // [-y]  Text^  [+y]
        CENTER(h -> -0.5 * h); // [-y]  Te^xt  [+y]

        private final Function<Double, Double> trMapper;

        public static final VerticalAlignment DEFAULT = VerticalAlignment.CENTER;

        VerticalAlignment(Function<Double, Double> trMapper) {
            this.trMapper = trMapper;
        }

        public double getTranslateY(double height) {
            return this.trMapper.apply(height);
        }
    }
}
