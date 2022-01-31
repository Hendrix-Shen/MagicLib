package top.hendrixshen.magiclib.untils.dependency;

import org.jetbrains.annotations.Nullable;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

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
@Target({ /* No targets allowed */})
@Retention(RetentionPolicy.RUNTIME)
public @interface Dependency {
    /**
     * Preset simple compatibility options
     */
    enum DependencyType {
        /**
         * Default behaviour, no interference with inspection content.
         */
        NORMAL,
        /**
         * If this item is used, the decorated class is not executed when the
         * {@link Dependency#modid}, {@link Dependency#version} and
         * {@link Dependency#predicate} (if present) checks pass.
         */
        CONFLICT
    }

    /**
     * Dependency mod id.
     *
     * @return mod id.
     */
    String modid();

    /**
     * Dependency mod version expressions.
     *
     * @return Dependency version expressions list.
     */
    String[] version();

    /**
     * Dependency mod type.
     *
     * @return Dependency type.
     */
    DependencyType dependencyType() default DependencyType.NORMAL;

    /**
     * Dependency custom predicate.
     *
     * @return Custom Predicate Classes.
     */
    @Nullable
    Class<? extends CustomDependencyPredicate>[] predicate() default {};
}
