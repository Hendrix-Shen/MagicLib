package top.hendrixshen.magiclib.untils.dependency;

import org.objectweb.asm.tree.ClassNode;

import java.util.function.Predicate;

/**
 * Custom predicates can be used in the annotation of validation dependencies
 * to perform additional checks.
 */
public interface CustomDependencyPredicate extends Predicate<ClassNode> {
}
