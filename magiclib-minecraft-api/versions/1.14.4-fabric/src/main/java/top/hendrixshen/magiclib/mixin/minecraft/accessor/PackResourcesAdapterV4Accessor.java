package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.15  : subproject 1.14.4 [dummy]        &lt;--------</li>
 * <li>mc1.16 ~ mc1.19.2: subproject 1.16.5 (main project)</li>
 * <li>mc1.19.3+        : subproject 1.19.3 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(DummyClass.class)
public interface PackResourcesAdapterV4Accessor {
}
