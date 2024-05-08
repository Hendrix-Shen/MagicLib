package top.hendrixshen.magiclib.render.impl;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.Tesselator;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import com.mojang.math.Matrix4f;
import top.hendrixshen.magiclib.compat.minecraft.api.network.chat.ComponentCompatApi;
import top.hendrixshen.magiclib.event.render.impl.RenderContext;
import top.hendrixshen.magiclib.util.RenderUtil;

import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

//#if MC < 11904
import com.mojang.blaze3d.vertex.PoseStack;
//#endif

//#if MC > 11404
import com.mojang.math.Transformation;
import net.minecraft.client.renderer.MultiBufferSource;
//#endif

public class TextRenderer {
    public static final double DEFAULT_FONT_SCALE = 0.025;

    private final List<Component> lines;
    private Vec3 pos;
    private double shiftX;
    private double shiftY;
    @Getter
    private double fontScale;
    private double lineHeightRatio;
    private int color;
    private int backgroundColor;
    private boolean shadow;
    private boolean seeThrough;
    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;

    private TextRenderer() {
        this.lines = Lists.newArrayList();
        this.shiftX = this.shiftY = 0.0;
        this.fontScale = DEFAULT_FONT_SCALE;
        this.lineHeightRatio = 1.0 * RenderUtil.TEXT_LINE_HEIGHT / RenderUtil.TEXT_HEIGHT;
        this.color = 0xFFFFFFFF;
        this.backgroundColor = 0x00000000;
        this.shadow = false;
        this.seeThrough = false;
        this.horizontalAlignment = HorizontalAlignment.DEFAULT;
        this.verticalAlignment = VerticalAlignment.DEFAULT;
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull TextRenderer create() {
        return new TextRenderer();
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    public void render(RenderContext context) {
        if (this.lines.isEmpty()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();

        //#if MC > 11605
        //$$ context = new RenderContext(RenderSystem.getModelViewStack());
        //#else
        context = new RenderContext(new PoseStack());
        //#endif
        CameraPositionTransformer transformer = new CameraPositionTransformer(this.pos);
        transformer.apply(context);

        context.scale(-this.fontScale, -this.fontScale, this.fontScale);
        //#if MC < 11700
        context.disableLighting();
        //#endif

        if (this.seeThrough) {
            context.disableDepthTest();
        } else {
            context.enableDepthTest();
        }

        //#if MC < 11904
        context.enableTexture();
        //#endif
        context.depthMask(true);
        int lineNum = this.lines.size();
        double maxTextWidth = this.lines.stream().mapToInt(RenderUtil::getRenderWidth).max().orElse(0);
        double totalTextWidth = maxTextWidth;
        double totalTextHeight = RenderUtil.TEXT_HEIGHT * lineNum + (this.lineHeightRatio - 1) * (lineNum - 1);
        context.translate(this.horizontalAlignment.getTranslateX(totalTextWidth), this.verticalAlignment.getTranslateY(totalTextHeight), 0);
        context.translate(this.shiftX, this.shiftY, 0);
        //#if MC > 11605
        //$$ RenderSystem.applyModelViewMatrix();
        //#else
        context.enableAlphaTest();
        //#endif
        context.enableBlend();
        context.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

        for (int i = 0; i < lineNum; i++) {
            Component text = this.lines.get(i);
            float textX = (float) this.horizontalAlignment.getTextX(maxTextWidth, RenderUtil.getRenderWidth(text));
            float textY = (float) (this.getLineHeight() * i);
            int backgroundColor = this.backgroundColor;

            while (true) {
                //#if MC > 11404
                MultiBufferSource.BufferSource source = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                //#if MC > 11605
                //$$ Matrix4f matrix4f = Transformation.identity().getMatrix();
                //#else
                Matrix4f matrix4f = context.getPoseStack().last().pose();
                //#endif
                //#if MC > 11903
                //$$ mc.font.drawInBatch(text, textX, textY, this.color, this.shadow, matrix4f, source, this.seeThrough ? Font.DisplayMode.SEE_THROUGH : Font.DisplayMode.NORMAL, backgroundColor, 0xF000F0);
                //#elseif MC > 11502
                mc.font.drawInBatch(text, textX, textY, this.color, this.shadow, matrix4f, source, this.seeThrough, backgroundColor, 0xF000F0);
                //#else
                //$$ mc.font.drawInBatch(text.getColoredString(), textX, textY, this.color, this.shadow, matrix4f, source, this.seeThrough, backgroundColor, 0xF000F0);
                //#endif
                source.endBatch();
                //#else
                //$$ mc.font.drawInBatch(text.getColoredString(), textX, textY, this.color, this.shadow, context.getPoseStack().last().pose(), this.seeThrough, backgroundColor, 0xF000F0);
                //#endif

                if (backgroundColor == 0) {
                    break;
                } else {
                    backgroundColor = 0;
                }
            }
        }

        //#if MC < 11600
        //$$ context.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        //#endif
        //#if MC < 11904
        context.enableDepthTest();
        //#endif
        transformer.restore();
        //#if MC > 11605
        //$$ RenderSystem.applyModelViewMatrix();
        //#endif
    }

    private TextRenderer addLines(Component... lines) {
        Collections.addAll(this.lines, lines);
        return this;
    }

    private TextRenderer setLines(Component... lines) {
        this.lines.clear();
        this.addLines(lines);
        return this;
    }

    public TextRenderer text(String text) {
        return this.text(ComponentCompatApi.literal(text));
    }

    public TextRenderer text(Component text) {
        return this.setLines(text);
    }

    public TextRenderer addLine(String text) {
        return this.addLines(ComponentCompatApi.literal(text));
    }

    public TextRenderer addLine(Component text) {
        return this.addLines(text);
    }

    public TextRenderer lineHeight(double lineHeightRatio) {
        this.lineHeightRatio = lineHeightRatio;
        return this;
    }

    public TextRenderer pos(double x, double y, double z) {
        return this.pos(new Vec3(x, y, z));
    }

    public TextRenderer pos(Vec3 pos) {
        this.pos = pos;
        return this;
    }

    public TextRenderer blockCenter(@NotNull BlockPos pos) {
        return this.pos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    public TextRenderer shift(double x, double y) {
        this.shiftX = x;
        this.shiftY = y;
        return this;
    }

    public TextRenderer fontScale(double fontScale) {
        this.fontScale = fontScale;
        return this;
    }

    public TextRenderer color(int color) {
        this.color = color;
        return this;
    }

    public TextRenderer bgColor(int backgroundColor) {
        this.backgroundColor = backgroundColor;
        return this;
    }

    public TextRenderer color(int color, int backgroundColor) {
        this.color(color);
        this.bgColor(backgroundColor);
        return this;
    }

    public TextRenderer shadow(boolean shadow) {
        this.shadow = shadow;
        return this;
    }

    public TextRenderer seeThrough(boolean seeThrough) {
        this.seeThrough = seeThrough;
        return this;
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

    public enum HorizontalAlignment {
        // [-x]  ^Text  [+x]
        LEFT(w -> 0.0, (w, tw) -> 0.0),
        // [-x]  Text^  [+x]
        RIGHT(w -> -w, (w, tw) -> w - tw),
        // [-x]  Te^xt  [+x]
        CENTER(w -> -0.5 * w, (w, tw) -> 0.5 * (w - tw));

        public static final HorizontalAlignment DEFAULT = CENTER;

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
        // [-y]  ^Text  [+y]
        TOP(h -> 0.0),
        // [-y]  Text^  [+y]
        BOTTOM(h -> -h),
        // [-y]  Te^xt  [+y]
        CENTER(h -> -0.5 * h);

        private final Function<Double, Double> trMapper;

        public static final VerticalAlignment DEFAULT = CENTER;

        VerticalAlignment(Function<Double, Double> trMapper) {
            this.trMapper = trMapper;
        }

        public double getTranslateY(double height) {
            return this.trMapper.apply(height);
        }
    }
}
