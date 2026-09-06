package top.hendrixshen.magiclib.api.dependency.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Declares one dependency group on an annotated element.
 *
 * <p>
 * A single group is satisfied when every entry in {@link Dependencies#require()} passes and every entry in
 * {@link Dependencies#conflict()} is not triggered. Multiple groups on the same element, either by repeating
 * {@code @Dependencies} or by using {@link CompositeDependencies}, are alternatives (logical or): the element
 * is satisfied when any group passes.
 * </p>
 */
@Repeatable(CompositeDependencies.class)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependencies {
    /**
     * The required dependencies of this group.
     *
     * <p>
     * All of them must be satisfied for the group to pass (logical and).
     * </p>
     *
     * @return The required dependencies.
     */
    Dependency[] require() default {};

    /**
     * The conflict dependencies of this group.
     *
     * <p>
     * The group fails when any of them is satisfied, so none of them may be triggered for the group to pass.
     * </p>
     *
     * @return The conflict dependencies.
     */
    Dependency[] conflict() default {};
}
