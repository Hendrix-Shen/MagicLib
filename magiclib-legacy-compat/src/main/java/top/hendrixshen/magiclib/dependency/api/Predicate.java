package top.hendrixshen.magiclib.dependency.api;

import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;

/**
 * A predicate class that will be constructed when parsing the {@link Dependency} annotation.
 */
@FunctionalInterface
public interface Predicate<T> {
    boolean isSatisfied(T t);
}