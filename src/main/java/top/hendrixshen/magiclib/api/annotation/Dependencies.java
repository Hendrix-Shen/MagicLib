package top.hendrixshen.magiclib.api.annotation;

import java.lang.annotation.Retention;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The main decorator for dependency checking, the classes decorated with this
 * annotation will perform these dependencies in our MixinPlugin.
 */
@Retention(RUNTIME)
public @interface Dependencies {
    /**
     * Apply Mixin only when conditions are satisfied.
     *
     * @return require dependencies list.
     */
    Dependency[] require() default {};

    /**
     * Cancel to apply Mixin when conditions are satisfied.
     *
     * @return conflict dependencies list.
     */
    Dependency[] conflict() default {};
}