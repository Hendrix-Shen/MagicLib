package top.hendrixshen.magiclib.api.malilib.annotation;

import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependencies;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    String category();

    Dependencies[] dependencies() default {};

    boolean debug() default false;

    boolean devOnly() default false;
}
