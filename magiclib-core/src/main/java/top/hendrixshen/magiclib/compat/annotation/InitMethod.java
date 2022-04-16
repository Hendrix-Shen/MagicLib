package top.hendrixshen.magiclib.compat.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MagicLibMixinPlugin auxiliary annotation.
 *
 * <p> Use Mixin to inject a new constructor method into the target class.
 *
 * <p> This method is only used to locate the original constructor method and works
 * similarly to the {@link org.spongepowered.asm.mixin.Shadow} functionality
 * provided by Mixin.
 *
 * <p> When this method is called for a method decorated with {@link ThisInitMethod},
 * the list of formal parameters of the method should be consistent with the list of
 * formal parameters of the constructor of the target class.
 *
 * <p> When this method is called for a method decorated with {@link SuperInitMethod},
 * the list of formal parameters of the method should be consistent with the list of
 * formal parameters of the constructor method of the superclass of the target class.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface InitMethod {
}