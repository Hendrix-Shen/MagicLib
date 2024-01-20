package top.hendrixshen.magiclib.api.mixin;

public interface MixinDependencyChecker {
    boolean check(String targetClassName, String mixinClassName);

    void setCheckFailureCallback(MixinDependencyCheckFailureCallback callback);
}
