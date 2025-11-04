package top.hendrixshen.magiclib.mixin.minecraft.compat;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.fake.compat.FontAccessor;
import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.15.2: subproject 1.15.2 [dummy]</li>
 * <li>mc1.15 ~ mc1.21.5: subproject 1.16.5 (main project)</li>
 * <li>mc1.21.6+        : subproject 1.21.8 [dummy]        &lt;--------</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(DummyClass.class)
public abstract class FontMixin implements FontAccessor {
}
