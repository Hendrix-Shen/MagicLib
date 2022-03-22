package top.hendrixshen.magiclib.config.annotation;

import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.OptionDependencyPredicate;
import top.hendrixshen.magiclib.util.Predicates;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    String category();

    Dependencies dependencies() default @Dependencies;

    Class<? extends OptionDependencyPredicate> predicate() default Predicates.TrueOptionPredicate.class;

}
