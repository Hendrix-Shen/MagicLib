package top.hendrixshen.magiclib.impl.render;

import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.render.context.RenderContext;

import java.util.Objects;

/**
 * Reference to <a href="https://github.com/Fallen-Breath/tweakermore">TweakerMore</a>
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
