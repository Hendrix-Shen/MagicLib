package top.hendrixshen.magiclib.dependency.mixin;

import top.hendrixshen.magiclib.dependency.DepCheckException;

@FunctionalInterface
public interface DepCheckFailureCallback {
    void callback(String targetClassName, String mixinClassName, DepCheckException reason);
}
