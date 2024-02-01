package top.hendrixshen.magiclib.api.dependency.annotation;

import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.DistType;
import top.hendrixshen.magiclib.util.collect.SimplePredicate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * <p>
 * This annotation class represents a valid dependency check where you can
 * quickly use a semantic versioning expression to check for the presence of
 * a specified dependency.
 * </p>
 *
 * <p>
 * Incompatible type settings are also preset, and additionally we support
 * custom predicates to help verify that the runtime environment is valid
 * for the annotated class.
 * </p>
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependency {
    /**
     * The input rules depend on the type.
     * <p>
     * A mod id if {@link Dependency#dependencyType()} == {@link DependencyType#MOD_ID}.
     * <p>
     * Means nothing if {@link Dependency#dependencyType()} == {@link DependencyType#PREDICATE}.
     */
    String value() default "";

    /**
     * Dependency check type.
     */
    DependencyType dependencyType() default DependencyType.MOD_ID;

    /**
     * Dist type.
     * <p>Only if the specified dist satisfied condition.
     */
    DistType distType() default DistType.ANY;

    /**
     * Semantic versioning expressions.
     * <p>
     * The value is used if {@link Dependency#dependencyType()} == {@link DependencyType#MOD_ID}
     * <br/> The condition is satisfied when the testing version matches any versionPredicate, or no
     * versionPredicate is given.
     */
    String[] versionPredicates() default {};

    /**
     * Specified your predicate class implemented.
     * <br/>This usually depends on your scenario, not simply the implementation of {@link SimplePredicate}
     * <p>
     * The value is used if {@link Dependency#dependencyType()} == {@link DependencyType#PREDICATE}
     */
    Class<? extends SimplePredicate> predicate() default SimplePredicate.class;

    /**
     * Optional dependency.
     * <p>
     * The value is used if {@link Dependency#dependencyType()} == {@link DependencyType#MOD_ID}
     */
    boolean optional() default false;
}
