package top.hendrixshen.magiclib.mixin.minecraft.i18n;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.19.2: subproject 1.16.5 (main project)</li>
 * <li>mc1.19.3+        : subproject 1.19.3 [dummy]        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(DummyClass.class)
public abstract class AbstractResourcePackMixin {
}
