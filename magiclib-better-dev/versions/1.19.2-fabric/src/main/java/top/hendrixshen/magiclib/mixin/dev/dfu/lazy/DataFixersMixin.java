package top.hendrixshen.magiclib.mixin.dev.dfu.lazy;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.19.1: subproject 1.16.5 (main project)</li>
 * <li>mc1.19.2+        : subproject 1.19.2 [dummy]        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(DummyClass.class)
public abstract class DataFixersMixin {
}
