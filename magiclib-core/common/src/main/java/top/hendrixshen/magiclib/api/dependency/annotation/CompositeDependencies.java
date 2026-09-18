package top.hendrixshen.magiclib.api.dependency.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * The container annotation that bundles multiple {@link Dependencies} declared on the same element.
 *
 * <p>
 * {@link Dependencies} is repeatable, so repeating it on an element is equivalent to declaring a
 * {@code @CompositeDependencies}. Every {@link Dependencies} in the value is an alternative: the annotated
 * element is satisfied when any of the groups passes (logical or).
 * </p>
 */
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CompositeDependencies {
    /**
     * The dependency groups declared on the element.
     *
     * <p>
     * Each group is satisfied when all of its required dependencies pass and none of its conflict
     * dependencies is triggered; the element is satisfied when any group passes.
     * </p>
     *
     * @return The dependency groups.
     */
    Dependencies[] value() default {};
}
