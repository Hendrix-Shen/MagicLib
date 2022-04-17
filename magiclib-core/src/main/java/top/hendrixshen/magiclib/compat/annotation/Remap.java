package top.hendrixshen.magiclib.compat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MagicLibMixinPlugin auxiliary annotation.
 *
 * <p>Used to manually obfuscate class/method/field names with unspecified values.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
public @interface Remap {
    /**
     * Obfuscation name.
     */
    String value();
    boolean dup() default false;
}