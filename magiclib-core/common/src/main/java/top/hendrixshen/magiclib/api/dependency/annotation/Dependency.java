package top.hendrixshen.magiclib.api.dependency.annotation;

import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.platform.DistType;
import top.hendrixshen.magiclib.api.platform.PlatformType;
import top.hendrixshen.magiclib.util.collect.SimplePredicate;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation class represents a valid dependency check where you can
 * quickly use a semantic versioning expression to check for the presence of
 * a specified dependency.
 *
 * <p>
 * Incompatible type settings are also preset, and additionally we support
 * custom predicates to help verify that the runtime environment is valid
 * for the annotated class.
 * </p>
 */
@Target({ /* No targets allowed */})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependency {
    /**
     * The input rules depend on the type.
     *
     * <p>
     * A mod id if {@link Dependency#dependencyType()} == {@link DependencyType#MOD_ID}.
     * </p>
     *
     * <p>
     * Means nothing if {@link Dependency#dependencyType()} == {@link DependencyType#PREDICATE}.
     * </p>
     */
    String value() default "";

    /**
     * Dependency check type.
     */
    DependencyType dependencyType() default DependencyType.MOD_ID;

    /**
     * Platform type.
     * <br/>The value is used if {@link Dependency#dependencyType()} == {@link DependencyType#PLATFORM}
     *
     * <p>
     * Only if the specified platform satisfied condition.
     * </p>
     */
    PlatformType platformType() default PlatformType.ANY;

    /**
     * Dist type.
     * <br/>
     * The value is used if {@link Dependency#dependencyType()} == {@link DependencyType#DIST}
     *
     * <p>
     * Only if the specified dist satisfied condition.
     * </p>
     */
    DistType distType() default DistType.ANY;

    /**
     * Semantic versioning expressions.
     * <br/>
     * The value is used if {@link Dependency#dependencyType()} == {@link DependencyType#MOD_ID}
     *
     * <p>
     * The condition is satisfied when the testing version matches any versionPredicate, or no
     * versionPredicate is given.
     * </p>
     */
    String[] versionPredicates() default {};

    /**
     * Specified your predicate class implemented.
     * <br/>This usually depends on your scenario, not simply the implementation of {@link SimplePredicate}
     *
     * <p>
     * The value is used if {@link Dependency#dependencyType()} == {@link DependencyType#PREDICATE}
     * </p>
     */
    Class<? extends SimplePredicate> predicate() default SimplePredicate.class;

    /**
     * Optional dependency.
     *
     * <p>
     * The value is used if {@link Dependency#dependencyType()} == {@link DependencyType#MOD_ID}
     * </p>
     */
    boolean optional() default false;
}
