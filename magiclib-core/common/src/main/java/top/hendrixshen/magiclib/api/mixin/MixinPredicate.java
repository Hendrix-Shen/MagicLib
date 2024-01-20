package top.hendrixshen.magiclib.api.mixin;

import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.api.dependency.SimplePredicate;

@FunctionalInterface
public interface MixinPredicate extends SimplePredicate<ClassNode> {
}
