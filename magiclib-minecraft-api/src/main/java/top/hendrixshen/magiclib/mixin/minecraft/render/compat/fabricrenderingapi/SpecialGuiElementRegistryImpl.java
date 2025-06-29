package top.hendrixshen.magiclib.mixin.minecraft.render.compat.fabricrenderingapi;

import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.api.preprocess.DummyClass;

// Used in mc1.21.6+
@Dependencies(require = @Dependency(value = "fabric-rendering-v1", versionPredicates = ">=0.127.0"))
@Mixin(DummyClass.class)
public abstract class SpecialGuiElementRegistryImpl {
}
