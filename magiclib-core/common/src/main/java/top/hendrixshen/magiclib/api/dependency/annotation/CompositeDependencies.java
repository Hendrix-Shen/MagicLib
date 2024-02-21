package top.hendrixshen.magiclib.api.dependency.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * CompositeDependencies annotation.
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface CompositeDependencies {
    /**
     * The dependencies located in this list are logical and.
     *
     * @return Dependencies list.
     */
    Dependencies[] value();
}
