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
    /**
     * Logic and.
     * <p>
     * The dependencies located in this list are logical and.
     *
     * @return Dependencies list.
     */
    Dependency[] and() default {};

    /**
     * Logic or.
     * <p>
     * The dependencies located in this list are logical or.
     *
     * @return Dependencies list.
     */
    Dependency[] or() default {};

    /**
     * Logic not.
     * <p>
     * The dependencies located in this list are logical not.
     *
     * @return Dependencies list.
     */
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