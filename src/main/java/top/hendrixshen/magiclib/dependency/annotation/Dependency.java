package top.hendrixshen.magiclib.dependency.annotation;

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
     * @return versionPredicate.
     */
    String versionPredicate() default "*";

    /**
     * Mark this dependency as optional
     *
     * @return True if this dependency is optional.
     */
    boolean optional() default false;
}
