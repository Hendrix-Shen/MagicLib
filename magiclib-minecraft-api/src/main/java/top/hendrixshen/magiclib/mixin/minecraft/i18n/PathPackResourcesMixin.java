package top.hendrixshen.magiclib.mixin.minecraft.i18n;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

/**
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.19.2  : subproject 1.16.5 (main project) [dummy]        &lt;--------</li>
 * <li>mc1.19.3 ~ mc1.20.1: subproject 1.19.3</li>
 * <li>mc1.20.2+          : subproject 1.20.2</li>
 */
@Mixin(DummyClass.class)
public abstract class PathPackResourcesMixin {
}
