package top.hendrixshen.magiclib.compat.api.annotation;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MagicLibMixinPlugin auxiliary annotation.
 *
 * <p>Injects the specified class as an inner class into the target class.
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InnerClass {
    /**
     * Outer class
     *
     * @return Class.
     */
    Class<?> value();
}
