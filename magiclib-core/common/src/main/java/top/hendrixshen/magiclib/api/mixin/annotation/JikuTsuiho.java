package top.hendrixshen.magiclib.api.mixin.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 時空追放.
 */
@Retention(RetentionPolicy.CLASS)
@Target({ElementType.FIELD, ElementType.METHOD})
public @interface JikuTsuiho {
    /**
     * Mixin class name.
     */
    String mixinClassName();

    /**
     * Injector class. For example, {@link org.spongepowered.asm.mixin.injection.Inject @Inject}.
     */
    Class<? extends Annotation> injectType();
}
