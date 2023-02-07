package top.hendrixshen.magiclib.compat.mixin.carpettisaddition;

//#if MC > 11802
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Dynamic;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.hendrixshen.magiclib.util.ReflectUtil;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
//#endif
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import top.hendrixshen.magiclib.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.annotation.Dependency;

@Pseudo
@Dependencies(
        and = {
                @Dependency(value = "minecraft", versionPredicate = ">1.18.2"),
                @Dependency(value = "carpet-tis-addition", versionPredicate = ">=1.38")
        }
)
@Mixin(targets = "carpettisaddition.settings.CarpetRuleRegistrar", remap = false)
public class MixinCarpetRuleRegistrar {
    //#if MC > 11802
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
                Field.class, ReflectUtil.getInnerClass(ReflectUtil.getClass("carpet.settings.ParsedRule"), "RuleAnnotation"),
                carpet.api.settings.SettingsManager.class}, parameters[0], parameters[1], parameters[2]);
    }
    //#endif
}
