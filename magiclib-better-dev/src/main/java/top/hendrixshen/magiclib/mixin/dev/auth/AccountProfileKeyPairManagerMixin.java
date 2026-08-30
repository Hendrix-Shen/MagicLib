package top.hendrixshen.magiclib.mixin.dev.auth;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.preprocess.DummyClass;

/**
 * Preprocessor version guide.
 *
 * <li>mc1.14 ~ mc1.19.2: subproject 1.16.5 (main project) [dummy]        &lt;--------</li>
 * <li>mc1.19.3+        : subproject 1.19.3</li>
 */
@Mixin(DummyClass.class)
public abstract class AccountProfileKeyPairManagerMixin {
}
