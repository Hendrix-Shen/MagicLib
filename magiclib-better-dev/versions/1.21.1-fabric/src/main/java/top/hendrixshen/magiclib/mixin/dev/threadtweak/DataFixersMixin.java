package top.hendrixshen.magiclib.mixin.dev.threadtweak;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.19.3: subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.19.4 ~ mc1.21: subproject 1.19.4</li>
 * <li>mc1.21.1+        : subproject 1.21.1 [dummy]        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(DummyClass.class)
public abstract class DataFixersMixin {
}
