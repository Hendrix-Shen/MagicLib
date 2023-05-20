package top.hendrixshen.magiclib.render.impl;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.Tesselator;
import lombok.Getter;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;
import top.hendrixshen.magiclib.compat.minecraft.api.network.chat.ComponentCompatApi;
import top.hendrixshen.magiclib.event.render.impl.RenderContext;

import java.util.Collections;
import java.util.List;

//#if MC < 11904
//$$ import com.mojang.blaze3d.systems.RenderSystem;
//$$ import com.mojang.blaze3d.vertex.PoseStack;
//#endif

//#if MC > 11404
import net.minecraft.client.renderer.MultiBufferSource;
//#endif

public class TextRenderer {
    private final List<Component> lines = Lists.newArrayList();
    private Vec3 pos = Vec3.ZERO;
    @Getter
    private double fontSize = 0.02;
    private double lineHeight = (Minecraft.getInstance().font.lineHeight + 1.0) / Minecraft.getInstance().font.lineHeight;
    private int color = 0xFFFFFFFF;
    private int backgroundColor = 0x00000000;
    private boolean shadow = false;
    private boolean seeThrough = false;

    @SuppressWarnings("UnnecessaryLocalVariable")
    public void render(RenderContext context) {
        if (this.lines.isEmpty()) {
            return;
        }

        Minecraft mc = Minecraft.getInstance();
        Camera camera = mc.gameRenderer.getMainCamera();

        if (camera.isInitialized() && mc.player != null) {
            double x = this.pos.x();
            double y = this.pos.y();
            double z = this.pos.z();
            double camX = camera.getPosition().x();
            double camY = camera.getPosition().y();
            double camZ = camera.getPosition().z();
            //#if MC < 11904
            //$$ context = new RenderContext(new PoseStack());
            //#endif
            context.pushMatrix();
            context.translate(x - camX, y - camY, z - camZ);
            //#if MC > 11902
            context.mulPoseMatrix(new Matrix4f().rotation(camera.rotationCompat()));
            //#else
            //$$ context.mulPoseMatrix(new Matrix4f(camera.rotationCompat()));
            //#endif
            context.scale(this.fontSize, -this.fontSize, this.fontSize);
            //#if MC < 11700
            //$$ context.disableLighting();
            //#endif

            if (this.seeThrough) {
                context.disableDepthTest();
            } else {
                context.enableDepthTest();
            }

            //#if MC < 11904
            //$$ context.enableTexture();
            //#endif
            context.depthMask(true);
            context.scale(-1.0, 1.0, 1.0);
            int lineNum = this.lines.size();
            double maxTextWidth = this.lines.stream().mapToInt(mc.font::width).max().orElse(0);
            double totalTextX = maxTextWidth;
            double totalTextY = mc.font.lineHeight * lineNum + (this.lineHeight - 1) * (lineNum - 1);
            context.translate(-totalTextX * 0.5, -totalTextY * 0.5, 0);
            //#if MC < 11904
            //#if MC > 11605
            //$$ RenderSystem.applyModelViewMatrix();
            //#else
            //$$ context.enableAlphaTest();
            //#endif
            //#endif
            context.enableBlend();
            context.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

            for (int i = 0; i < lineNum; i++) {
                Component text = this.lines.get(i);
                float textX = (float)((maxTextWidth - mc.font.widthCompat(text)) / 2);
                float textY = (float)(mc.font.lineHeight * this.lineHeight * i);

                int backgroundColor = this.backgroundColor;

                while (true) {
                    //#if MC > 11404
                    MultiBufferSource.BufferSource source = MultiBufferSource.immediate(Tesselator.getInstance().getBuilder());
                    //#endif
                    Matrix4f matrix4f = context.getPoseStack().last().pose();
                    //#if MC > 11502
                    mc.font.drawInBatch(text, textX, textY, this.color, this.shadow, matrix4f, source,
                    //#elseif MC > 11404
                    //$$ mc.font.drawInBatch(text.getColoredString(), textX, textY, this.color, this.shadow, matrix4f, source,
                    //#else
                    //$$ mc.font.drawInBatch(text.getColoredString(), textX, textY, this.color, this.shadow, matrix4f,
                    //#endif
                            //#if MC > 11903
                            this.seeThrough ? net.minecraft.client.gui.Font.DisplayMode.SEE_THROUGH : net.minecraft.client.gui.Font.DisplayMode.NORMAL,
                            //#else
                            //$$ this.seeThrough,
                            //#endif
                            backgroundColor, 0xF000F0);
                    //#if MC > 11404
                    source.endBatch();
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
            //$$ context.enableDepthTest();
            //#endif
            context.popMatrix();
        }
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

    public TextRenderer lineHeight(double lineHeight) {
        this.lineHeight = lineHeight;
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

    public TextRenderer fontSize(double fontSize) {
        this.fontSize = fontSize;
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
}
