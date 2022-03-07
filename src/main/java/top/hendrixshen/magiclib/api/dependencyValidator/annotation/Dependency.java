package top.hendrixshen.magiclib.api.dependencyValidator.annotation;

import org.jetbrains.annotations.Nullable;

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
     * Dependency mod.
     *
     * @return mod id.
     */
    String value();

    /**
     * Semantic versioning expressions.
     *
     * @return predicates list.
     */
    String[] versionPredicates();

    /**
     * Dependency custom predicate.
     *
     * @return Custom Predicate Classes.
     */
    @Nullable
    Class<? extends CustomDependencyPredicate>[] predicate() default {};
}
