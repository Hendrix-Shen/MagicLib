package top.hendrixshen.magiclib.dependency.annotation;

import org.objectweb.asm.tree.ClassNode;

import java.util.function.Predicate;

/**
 * Custom predicates can be used in the annotation of validation dependencies
 * to perform additional checks for mixin.
 */
public interface MixinDependencyPredicate extends Predicate<ClassNode> {
}
