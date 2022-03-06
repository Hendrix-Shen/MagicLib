package top.hendrixshen.magiclib.api.mixin;

@FunctionalInterface
public interface DependencyValidateFailureCallback {
    void callback(String mixinClassName, String reason);
}
