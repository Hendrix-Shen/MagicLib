package top.hendrixshen.magiclib.untils.dependency;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.CLASS;

/**
 * The main decorator for dependency checking, the classes decorated with this
 * annotation will perform these dependencies in our MixinPlugin
 */
@Retention(CLASS)
@Target(TYPE)
public @interface Dependencies {
    /**
     * Dependencies of this Mixin
     *
     * @return dependencyList
     */
    Dependency[] dependencyList();
}