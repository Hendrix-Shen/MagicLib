package top.hendrixshen.magiclib.render.impl;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.network.chat.ComponentCompat;
import top.hendrixshen.magiclib.event.render.impl.RenderContext;

@Deprecated
@ApiStatus.ScheduledForRemoval
public class TextRenderer {
    public static final double DEFAULT_FONT_SCALE = top.hendrixshen.magiclib.impl.render.TextRenderer.DEFAULT_FONT_SCALE;
    private final top.hendrixshen.magiclib.impl.render.TextRenderer renderer;

    private TextRenderer() {
        this.renderer = top.hendrixshen.magiclib.impl.render.TextRenderer.create();
    }

    @Contract(value = " -> new", pure = true)
    public static @NotNull TextRenderer create() {
        return new TextRenderer();
    }

    public void render(RenderContext context) {
        this.renderer.render();
    }

    private TextRenderer addLines(Component... lines) {
        for (Component line : lines) {
            this.renderer.addLine(line);
        }

        return this;
    }

    public TextRenderer text(String text) {
        return this.text(ComponentCompat.literal(text).get());
    }

    public TextRenderer text(Component text) {
        this.renderer.text(text);
        return this;
    }

    public TextRenderer addLine(String text) {
        return this.addLines(ComponentCompat.literal(text).get());
    }

    public TextRenderer addLine(Component text) {
        return this.addLines(text);
    }

    public TextRenderer lineHeight(double lineHeightRatio) {
        this.renderer.lineHeightRatio(lineHeightRatio);
        return this;
    }

    public TextRenderer pos(double x, double y, double z) {
        return this.pos(new Vec3(x, y, z));
    }

    public TextRenderer pos(Vec3 pos) {
        this.renderer.at(pos);
        return this;
    }

    public TextRenderer blockCenter(@NotNull BlockPos pos) {
        return this.pos(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5);
    }

    public TextRenderer shift(double x, double y) {
        this.renderer.shift(x, y);
        return this;
    }

    public TextRenderer fontScale(double fontScale) {
        this.renderer.fontScale(fontScale);
        return this;
    }

    public TextRenderer color(int color) {
        this.renderer.color(color);
        return this;
    }

    public TextRenderer bgColor(int backgroundColor) {
        this.renderer.bgColor(backgroundColor);
        return this;
    }

    public TextRenderer color(int color, int backgroundColor) {
        this.color(color);
        this.bgColor(backgroundColor);
        return this;
    }

    public TextRenderer shadow(boolean shadow) {
        this.renderer.shadow(shadow);
        return this;
    }

    public TextRenderer seeThrough(boolean seeThrough) {
        this.renderer.seeThrough(seeThrough);
        return this;
    }

    public TextRenderer align(HorizontalAlignment horizontalAlignment) {
        this.renderer.align(horizontalAlignment.inner);
        return this;
    }

    public TextRenderer align(VerticalAlignment verticalAlignment) {
        this.renderer.align(verticalAlignment.inner);
        return this;
    }


    public double getLineHeight() {
        return this.renderer.getLineHeight();
    }

    public enum HorizontalAlignment {
        // [-x]  ^Text  [+x]
        LEFT(top.hendrixshen.magiclib.impl.render.TextRenderer.HorizontalAlignment.LEFT),
        // [-x]  Text^  [+x]
        RIGHT(top.hendrixshen.magiclib.impl.render.TextRenderer.HorizontalAlignment.RIGHT),
        // [-x]  Te^xt  [+x]
        CENTER(top.hendrixshen.magiclib.impl.render.TextRenderer.HorizontalAlignment.CENTER);

        public static final HorizontalAlignment DEFAULT = CENTER;

        private final top.hendrixshen.magiclib.impl.render.TextRenderer.HorizontalAlignment inner;

        HorizontalAlignment(top.hendrixshen.magiclib.impl.render.TextRenderer.HorizontalAlignment inner) {
            this.inner = inner;
        }

        public double getTranslateX(double width) {
            return this.inner.getTranslateX(width);
        }

        public double getTextX(double width, double textWidth) {
            return this.inner.getTextX(width, textWidth);
        }
    }

    public enum VerticalAlignment {
        // [-y]  ^Text  [+y]
        TOP(top.hendrixshen.magiclib.impl.render.TextRenderer.VerticalAlignment.TOP),
        // [-y]  Text^  [+y]
        BOTTOM(top.hendrixshen.magiclib.impl.render.TextRenderer.VerticalAlignment.BOTTOM),
        // [-y]  Te^xt  [+y]
        CENTER(top.hendrixshen.magiclib.impl.render.TextRenderer.VerticalAlignment.CENTER);

        private final top.hendrixshen.magiclib.impl.render.TextRenderer.VerticalAlignment inner;

        VerticalAlignment(top.hendrixshen.magiclib.impl.render.TextRenderer.VerticalAlignment inner) {
            this.inner = inner;
        }

        public double getTranslateY(double height) {
            return this.inner.getTranslateY(height);
        }
    }
}
