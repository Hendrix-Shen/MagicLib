package top.hendrixshen.magiclib.dependency.api;

import top.hendrixshen.magiclib.malilib.impl.ConfigOption;

/**
 * Custom predicates can be used in the annotation of validation dependencies
 * to perform additional checks for config.
 */
public interface ConfigDependencyPredicate extends Predicate<ConfigOption> {
}
