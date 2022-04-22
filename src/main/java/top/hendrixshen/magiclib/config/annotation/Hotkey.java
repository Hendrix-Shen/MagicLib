package top.hendrixshen.magiclib.config.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Binding hotkey for configuration.
 * <p>This annotation is only valid when decorating {@link fi.dy.masa.malilib.config.options.ConfigHotkey}
 * instances and {@link fi.dy.masa.malilib.config.options.ConfigBooleanHotkeyed} instances.
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Hotkey {
    /**
     * Hotkey binding.
     *
     * @return A Key bind for configuration.
     */
    String hotkey() default "";
}