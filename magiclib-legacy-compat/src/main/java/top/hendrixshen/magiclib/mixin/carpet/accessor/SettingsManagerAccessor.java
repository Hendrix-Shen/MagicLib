package top.hendrixshen.magiclib.mixin.carpet.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;

//#if MC > 11502
import java.nio.file.Path;
//#else
//$$ import org.apache.commons.lang3.tuple.Pair;
//#endif

//#if MC > 11900
//$$ import carpet.api.settings.SettingsManager;
//#else
import carpet.settings.ParsedRule;
import carpet.settings.SettingsManager;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.Map;
//#endif

@CompositeDependencies(@Dependencies(require = @Dependency(dependencyType = DependencyType.MOD_ID, value = "carpet")))
@Mixin(value = SettingsManager.class, remap = false)
public interface SettingsManagerAccessor {
    //#if MC < 11901
    @Accessor
    Map<String, ParsedRule<?>> getRules();

    @Invoker
    void invokeWriteSettingsToConf(Map<String, String> values);
    //#endif

    //#if MC > 11502
    @Invoker
    Path invokeGetFile();
    //#else
    //$$ @Invoker
    //$$ Pair<Map<String, String>, Boolean> invokeReadSettingsFromConf();
    //#endif
}
