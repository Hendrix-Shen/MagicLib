package top.hendrixshen.magiclib.api.mixin.annotation;

import java.lang.annotation.*;

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
