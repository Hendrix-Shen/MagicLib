package top.hendrixshen.magiclib.api.dependency.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Dependencies annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependencies {
    /**
     * All conditions satisfied, test passed.
     * <p>
     * The dependencies located in this list are logical or.
     *
     * @return True if all conditions are satisfied, otherwise false.
     */
    Dependency[] require() default {};

    /**
     * Any conditions satisfied, test fails.
     * <p>
     * The dependencies located in this list are logical or.
     *
     * @return True if none of the conditions are satisfied, otherwise false.
     */
    Dependency[] conflict() default {};
}
