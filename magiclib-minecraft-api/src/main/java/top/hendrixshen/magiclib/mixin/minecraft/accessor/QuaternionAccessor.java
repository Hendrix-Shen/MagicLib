package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.15: subproject 1.14.4</li>
 * <li>mc1.16+        : subproject 1.16.5 (main project) [dummy]        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(DummyClass.class)
public interface QuaternionAccessor {
}
