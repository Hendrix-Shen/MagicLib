package top.hendrixshen.magiclib.compat.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface InnerClass {
    Class<?> value();
}
