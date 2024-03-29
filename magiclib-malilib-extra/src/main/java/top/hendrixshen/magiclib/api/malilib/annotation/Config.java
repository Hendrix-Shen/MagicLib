package top.hendrixshen.magiclib.api.malilib.annotation;


import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Config {
    /**
     * The category to which the configuration belongs, if using the GUI provided by MagicLib,
     * will be used as the basis for tab assignment.
     *
     * @return Configuration category.
     */
    String category() default Config.defaultCategory;

    CompositeDependencies compositeDependencies() default @CompositeDependencies();

    String defaultCategory = "all";
}
