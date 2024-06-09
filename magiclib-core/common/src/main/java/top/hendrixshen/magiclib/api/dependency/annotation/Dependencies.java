package top.hendrixshen.magiclib.api.dependency.annotation;

import java.lang.annotation.*;

/**
 * Dependencies annotation.
 */
@Repeatable(CompositeDependencies.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
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
