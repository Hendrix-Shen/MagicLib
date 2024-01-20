package top.hendrixshen.magiclib.api.dependency.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The main decorator for dependency checking, the classes decorated with this annotation will perform these
 * dependencies in our MixinPlugin or ConfigManager etc.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependencies {
    /**
     * All conditions satisfied, test passed
     */
    Dependency[] require() default {};

    /**
     * Any condition is satisfied, the test fails
     */
    Dependency[] conflict() default {};
}
