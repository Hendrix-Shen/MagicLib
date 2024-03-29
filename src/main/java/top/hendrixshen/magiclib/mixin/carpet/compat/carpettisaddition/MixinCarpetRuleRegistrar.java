package top.hendrixshen.magiclib.mixin.carpet.compat.carpettisaddition;

import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;
import top.hendrixshen.magiclib.util.ReflectUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

@Pseudo
@Dependencies(
        and = {
                @Dependency("carpet"),
                @Dependency(value = "carpet-tis-addition", versionPredicate = ">=1.38")
        }
)
@Mixin(targets = "carpettisaddition.settings.CarpetRuleRegistrar", remap = false)
public class MixinCarpetRuleRegistrar {
    @Dynamic
    @Redirect(
            method = "parseRule",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/reflect/Constructor;newInstance([Ljava/lang/Object;)Ljava/lang/Object;",
                    ordinal = 1
            ),
            require = 0
    )
    //Parameters: Field field, Object ruleAnnotation, carpet.api.settings.SettingsManager settingsManager
    private @NotNull Object redirectConstructorCall(Constructor<?> constructor, Object @NotNull ... parameters) {
        return ReflectUtil.newInstance("carpet.settings.ParsedRule", new Class[]{
                Field.class, ReflectUtil.getInnerClass(ReflectUtil.getClass("carpet.settings.ParsedRule").orElseThrow(RuntimeException::new), "RuleAnnotation")
                .orElseThrow(RuntimeException::new), carpet.api.settings.SettingsManager.class}, parameters[0], parameters[1], parameters[2]).orElseThrow(RuntimeException::new);
    }
}
