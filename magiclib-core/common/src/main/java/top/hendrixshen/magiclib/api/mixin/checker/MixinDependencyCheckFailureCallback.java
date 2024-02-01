package top.hendrixshen.magiclib.api.mixin.checker;

import top.hendrixshen.magiclib.api.dependency.DependencyCheckException;

@FunctionalInterface
public interface MixinDependencyCheckFailureCallback {
    void callback(String targetClassName, String mixinClassName, DependencyCheckException reason);
}
