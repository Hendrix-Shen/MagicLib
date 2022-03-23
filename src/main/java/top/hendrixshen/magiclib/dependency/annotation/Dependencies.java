package top.hendrixshen.magiclib.dependency.annotation;

import java.lang.annotation.Retention;
import java.util.function.Predicate;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * The main decorator for dependency checking, the classes decorated with this
 * annotation will perform these dependencies in our MixinPlugin.
 */
@Retention(RUNTIME)
public @interface Dependencies {

    Dependency[] and() default {};

    Dependency[] or() default {};

    Dependency[] not() default {};

    /**
     * Dependency custom predicate.
     *
     * @return Custom Predicate Classes.
     */
    Class<? extends Predicate<?>> predicate() default DefaultPredicate.class;

    class DefaultPredicate implements Predicate<Object> {
        @Override
        public boolean test(Object option) {
            return true;
        }
    }
}