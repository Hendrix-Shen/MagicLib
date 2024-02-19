package top.hendrixshen.magiclib.api.mixin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MagicInit annotation.
 *
 * <p>
 * This method is only used to locate the original constructor method and works
 * similarly to the {@link org.spongepowered.asm.mixin.Shadow} functionality
 * provided by Mixin.
 *
 * <p>
 * When this method is called for a method decorated with {@link ThisInit},
 * the list of formal parameters of the method should be consistent with the list of
 * formal parameters of the constructor of the target class.
 *
 * <p>
 * When this method is called for a method decorated with {@link SuperInit},
 * the list of formal parameters of the method should be consistent with the list of
 * formal parameters of the constructor method of the superclass of the target class.
 *
 * <p>
 * <b>NOTE: We won't check if the shadow has an entity, if it doesn't, the magic will get out of hand.</b>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface MagicInit {
}
