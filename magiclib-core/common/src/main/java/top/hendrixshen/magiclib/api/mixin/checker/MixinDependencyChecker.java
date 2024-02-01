package top.hendrixshen.magiclib.api.mixin.checker;

public interface MixinDependencyChecker {
    boolean check(String targetClassName, String mixinClassName);

    void setCheckFailureCallback(MixinDependencyCheckFailureCallback callback);
}
