package top.hendrixshen.magiclib.dependency.api;

import top.hendrixshen.magiclib.carpet.impl.RuleOption;

/**
 * Custom predicates can be used in the annotation of validation dependencies
 * to perform additional checks for rule.
 */
public interface RuleDependencyPredicate extends Predicate<RuleOption> {
}
