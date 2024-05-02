package top.hendrixshen.magiclib.carpet.api.annotation;

import top.hendrixshen.magiclib.carpet.api.Validator;
import top.hendrixshen.magiclib.dependency.api.RuleDependencyPredicate;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.impl.RuleDependencyPredicates;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Rule {
    String[] categories();

    String[] options() default {};

    boolean strict() default true;

    String appSource() default "";

    Class<? extends Validator<?>>[] validators() default {};

    Dependencies dependencies() default @Dependencies;

    Class<? extends RuleDependencyPredicate> predicate() default RuleDependencyPredicates.TrueRulePredicate.class;
}
