package top.hendrixshen.magiclib.mixin.minecraft.event.render;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

/**
 * Preprocessor version guide.
 *
 * <li>mc1.14           : subproject 1.14.4 [dummy]        &lt;--------</li>
 * <li>mc1.15 ~ mc1.19.3: subproject 1.16.5 (main project)</li>
 * <li>mc1.19.4+        : subproject 1.19.4</li>
 */
@Mixin(DummyClass.class)
public abstract class LevelRendererMixin {
}
