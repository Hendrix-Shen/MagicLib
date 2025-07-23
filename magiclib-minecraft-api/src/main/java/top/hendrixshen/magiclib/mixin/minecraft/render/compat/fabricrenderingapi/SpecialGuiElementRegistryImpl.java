package top.hendrixshen.magiclib.mixin.minecraft.render.compat.fabricrenderingapi;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.21.5: subproject 1.16.5 (main project) [dummy]        &lt;--------</li>
 * <li>mc1.21.6+        : subproject 1.21.8</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Dependencies(require = @Dependency(value = "fabric-rendering-v1", versionPredicates = ">=0.127.0"))
@Mixin(DummyClass.class)
public abstract class SpecialGuiElementRegistryImpl {
}
