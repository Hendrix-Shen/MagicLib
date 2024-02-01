package top.hendrixshen.magiclib.api.mixin.annotation;

import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * The main decorator for mixin dependency checking.
 * {@link top.hendrixshen.magiclib.impl.mixin.MagicMixinPlugin MagicMixinPlugin} will check the dependencies
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface MagicMixinConfig {
    /**
     * The dependencies located in this list are logical and.
     *
     * @return Dependencies list.
     */
    Dependencies[] dependencies();
}
