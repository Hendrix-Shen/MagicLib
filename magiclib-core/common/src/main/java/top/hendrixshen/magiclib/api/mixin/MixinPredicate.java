package top.hendrixshen.magiclib.api.mixin;

import org.objectweb.asm.tree.ClassNode;
import top.hendrixshen.magiclib.util.collect.SimplePredicate;

@FunctionalInterface
public interface MixinPredicate extends SimplePredicate<ClassNode> {
}
