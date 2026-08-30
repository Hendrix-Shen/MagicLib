package top.hendrixshen.magiclib.mixin.dev.dfu.lazy;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

/**
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.21: subproject 1.16.5 (main project) [dummy]        &lt;--------</li>
 * <li>mc1.21.1+      : subproject 1.21.1</li>
 */
@Mixin(DummyClass.class)
public abstract class MainMixin {
}
