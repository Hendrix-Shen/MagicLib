package top.hendrixshen.magiclib.compat.preprocess.api.annotation;

import org.jetbrains.annotations.ApiStatus;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Deprecated
@ApiStatus.ScheduledForRemoval
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Preprocess {
}
