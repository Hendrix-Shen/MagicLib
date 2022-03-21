package top.hendrixshen.magiclib.api.malilib.annotation;

import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.OptionDependencyPredicate;
import top.hendrixshen.magiclib.util.malilib.Option;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    String category();

    Dependencies dependencies() default @Dependencies;

    Class<? extends OptionDependencyPredicate> predicate() default OptionDependencyPredicate.class;

}
