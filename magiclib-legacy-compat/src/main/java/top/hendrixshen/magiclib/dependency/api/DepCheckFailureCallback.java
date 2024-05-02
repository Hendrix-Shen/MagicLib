package top.hendrixshen.magiclib.dependency.api;

import top.hendrixshen.magiclib.dependency.api.DepCheckException;

@FunctionalInterface
public interface DepCheckFailureCallback {
    void callback(String targetClassName, String mixinClassName, DepCheckException reason);
}
