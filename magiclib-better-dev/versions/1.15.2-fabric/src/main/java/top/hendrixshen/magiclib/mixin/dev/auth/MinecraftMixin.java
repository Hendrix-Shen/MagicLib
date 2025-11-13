package top.hendrixshen.magiclib.mixin.dev.auth;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.15  : subproject 1.15.2 [dummy]        &lt;--------</li>
 * <li>mc1.16 ~ mc1.20.3: subproject 1.16.5 (main project)</li>
 * <li>mc1.20.4+        : subproject 1.20.4 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(DummyClass.class)
public abstract class MinecraftMixin {
}
