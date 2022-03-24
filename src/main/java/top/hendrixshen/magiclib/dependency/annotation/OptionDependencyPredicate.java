package top.hendrixshen.magiclib.dependency.annotation;

import top.hendrixshen.magiclib.config.Option;

import java.util.function.Predicate;

/**
 * Custom predicates can be used in the annotation of validation dependencies
 * to perform additional checks for config.
 */
public interface OptionDependencyPredicate extends Predicate<Option> {
}
