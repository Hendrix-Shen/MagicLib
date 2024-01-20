package top.hendrixshen.magiclib.api.mixin.annotation;

import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface MagicMixinConfig {
    Dependencies[] dependencies();
}
