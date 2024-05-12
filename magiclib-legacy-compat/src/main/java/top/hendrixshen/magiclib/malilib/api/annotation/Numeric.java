package top.hendrixshen.magiclib.malilib.api.annotation;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Extra settings for configuration.
 * <p>This annotation is only valid when decorating {@link fi.dy.masa.malilib.config.options.ConfigInteger}
 * instances and {@link fi.dy.masa.malilib.config.options.ConfigDouble} instances.
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
@Environment(EnvType.CLIENT)
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Numeric {
    /**
     * Max value.
     *
     * @return Maximum value for configuration.
     */
    double maxValue();

    /**
     * Min value.
     *
     * @return Minimum value for configuration.
     */
    double minValue();

    /**
     * Whether to use a slider
     *
     * @return True if slider is used.
     */
    boolean useSlider() default false;
}
