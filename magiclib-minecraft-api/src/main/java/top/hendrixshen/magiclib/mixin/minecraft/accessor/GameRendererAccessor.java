package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

/**
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.21.5   : subproject 1.16.5 (main project) [dummy]        &lt;--------</li>
 * <li>mc1.21.6 ~ mc1.26.11: subproject 1.21.8</li>
 * <li>mc26.1+             : subproject 26.1.2 [dummy]</li>
 */
@Mixin(DummyClass.class)
public interface GameRendererAccessor {
}
