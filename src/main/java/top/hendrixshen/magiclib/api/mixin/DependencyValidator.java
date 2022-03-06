package top.hendrixshen.magiclib.api.mixin;

import top.hendrixshen.magiclib.api.annotation.Dependencies;

/**
 * A helper class to for checking if {@link Dependencies} annotation on a mixin class is satisfied.
 */
public interface DependencyValidator {
    /**
     * Check if the {@link Dependencies} annotation of the given mixin class is satisfied.
     *
     * @param mixinClassName the target mixin class name to check dependency.
     * @return true if the mixin class doesn't have the {@link Dependencies} annotation.
     */
    boolean checkDependency(String mixinClassName, String targetClassName);

    /**
     * Set the callback function when the restriction check fails.
     */
    void setFailureCallback(DependencyValidateFailureCallback failureCallback);
}
