package top.hendrixshen.magiclib.mixin.compat.cloth.renderSystem;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;

import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;

@Dependencies(require = @Dependency(value = "cloth-config2"))
@Pseudo
@Mixin(targets = "me.shedaniel.math.compat.RenderHelper", remap = false)
public class RenderHelperMixin {
    @Dynamic
    @WrapOperation(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Class;forName(Ljava/lang/String;)Ljava/lang/Class;",
                    ordinal = 0
            )
    )
    // We need to hide RenderSystem from cloth-api, which shouldn't be here.
    private static Class<?> patchReflection(String className, Operation<Class<?>> original) throws ClassNotFoundException {
        throw new ClassNotFoundException("com.mojang.blaze3d.systems.RenderSystem");
    }
}
