package top.hendrixshen.magiclib.api.rule.annotation;

import top.hendrixshen.magiclib.dependency.Predicates;
import top.hendrixshen.magiclib.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.annotation.RuleDependencyPredicate;
import top.hendrixshen.magiclib.api.rule.Validator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface Rule {
    String[] categories();

    String[] options() default {};

    boolean strict() default true;

    String appSource() default "";

    Class<? extends Validator>[] validators() default {};

    Dependencies dependencies() default @Dependencies;

    Class<? extends RuleDependencyPredicate> predicate() default Predicates.TrueRulePredicate.class;
}
