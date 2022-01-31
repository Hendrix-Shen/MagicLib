package top.hendrixshen.magiclib.untils.dependency;

import org.objectweb.asm.tree.ClassNode;

import java.util.function.Predicate;

public interface CustomDependencyPredicate extends Predicate<ClassNode> {
}
