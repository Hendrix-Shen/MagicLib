package top.hendrixshen.magiclib.dependency.api;

import org.objectweb.asm.tree.ClassNode;

/**
 * Custom predicates can be used in the annotation of validation dependencies
 * to perform additional checks for mixin.
 */
public interface MixinDependencyPredicate extends Predicate<ClassNode> {
}
