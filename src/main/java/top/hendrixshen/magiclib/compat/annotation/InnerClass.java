package top.hendrixshen.magiclib.compat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MagicLibMixinPlugin auxiliary annotation.
 *
 * <p>Injects the specified class as an inner class into the target class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface InnerClass {
    /**
     * Outter class
     *
     * @return Class.
     */
    Class<?> value();
}
