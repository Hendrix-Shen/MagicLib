package top.hendrixshen.magiclib.mixin.dev.threadtweak;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.dev.MixinPredicates;
import top.hendrixshen.magiclib.impl.dev.threadtweak.ThreadTweaker;

//#if MC > 11502
import net.minecraft.server.Main;
//#else
//$$ import net.minecraft.server.MinecraftServer;
//#endif

@CompositeDependencies(
        @Dependencies(
                require = @Dependency(
                        dependencyType = DependencyType.PREDICATE,
                        predicate = MixinPredicates.TheadTweakPredicate.class
                )
        )
)
@Mixin(
        //#if MC > 11502
        Main.class
        //#else
        //$$ MinecraftServer.class
        //#endif
)
public class MinecraftServerMixin {
    @Inject(
            method = "main",
            at = @At(
                    value = "HEAD"
            ),
            remap = false
    )
    private static void onMain(String[] strings, CallbackInfo ci) {
        Thread.currentThread().setPriority(ThreadTweaker.getGamePriority());
    }
}
