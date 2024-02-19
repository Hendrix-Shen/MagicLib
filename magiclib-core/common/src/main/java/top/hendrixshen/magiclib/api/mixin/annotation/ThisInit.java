package top.hendrixshen.magiclib.api.mixin.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * MagicInit annotation.
 *
 * <p>
 * This method is a shadow of the target class constructor
 * and is not really mixin the target class.
 *
 * <p>
 * When a method decorated by {@link MagicInit} is called in this method,
 * it means that the corresponding constructor method in the target class is called.
 *
 * <p>
 * <b>NOTE: We won't check if the shadow has an entity, if it doesn't, the magic will get out of hand.</b>
 */
@Retention(RetentionPolicy.CLASS)
@Target(ElementType.METHOD)
public @interface ThisInit {
}
