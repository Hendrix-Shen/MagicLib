package top.hendrixshen.magiclib.compat.api.annotation;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MagicLibMixinPlugin auxiliary annotation.
 *
 * <p> This method is used to add a new constructor to the target method.
 *
 * <p> When a method decorated by {@link InitMethod} is called in this method,
 * it means that the corresponding constructor method in the target class is called.
 */
@Deprecated
@ApiStatus.ScheduledForRemoval
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ThisInitMethod {
}