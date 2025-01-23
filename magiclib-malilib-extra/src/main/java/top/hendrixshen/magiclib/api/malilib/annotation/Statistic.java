package top.hendrixshen.magiclib.api.malilib.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Statistic {
    boolean hotkey() default true;

    boolean valueChanged() default true;
}
