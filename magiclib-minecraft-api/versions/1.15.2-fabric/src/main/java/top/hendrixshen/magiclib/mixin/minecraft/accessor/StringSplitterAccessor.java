package top.hendrixshen.magiclib.mixin.minecraft.accessor;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

/**
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.15.2: subproject 1.15.2 [dummy]        &lt;--------</li>
 * <li>mc1.16.5+        : subproject 1.16.5 (main project)</li>
 */
@Mixin(DummyClass.class)
public interface StringSplitterAccessor {
}
