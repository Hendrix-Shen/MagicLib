package top.hendrixshen.magiclib.api.malilib.annotation;

import java.lang.annotation.*;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Statistic {
    boolean hotkey() default true;

    boolean valueChanged() default true;
}
