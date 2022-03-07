package top.hendrixshen.magiclib.api.dependencyValidator.mixin;

@FunctionalInterface
public interface DependencyValidateFailureCallback {
    void callback(String mixinClassName, String reason);
}
