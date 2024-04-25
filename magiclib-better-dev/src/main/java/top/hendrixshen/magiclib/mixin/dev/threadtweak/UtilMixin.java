package top.hendrixshen.magiclib.mixin.dev.threadtweak;

import net.minecraft.Util;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.dev.MixinPredicates;
import top.hendrixshen.magiclib.impl.dev.threadtweak.ThreadTweaker;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

@CompositeDependencies(
        @Dependencies(
                require = @Dependency(
                        dependencyType = DependencyType.PREDICATE,
                        predicate = MixinPredicates.TheadTweakPredicate.class
                )
        )
)
@Mixin(Util.class)
public class UtilMixin {
    //#if MC > 11502
    //#if MC > 12003
    //$$ @Unique
    //$$ private static boolean magiclib$initDownloadWorker = false;
    //#endif
    //#if MC < 11904
    @Unique
    private static boolean magiclib$initBootstrapWorker = false;
    //#endif
    @Unique
    private static boolean magiclib$initIoWorker = false;
    //#endif
    @Unique
    private static boolean magiclib$initMainWorker = false;

    //#if MC > 11502
    //#if MC > 12003
    //$$ @Mutable
    //$$ @Shadow
    //$$ @Final
    //$$ private static ExecutorService DOWNLOAD_POOL;
    //#endif
    //#if MC < 11904
    @Mutable
    @Shadow
    @Final
    private static ExecutorService BOOTSTRAP_EXECUTOR;
    //#endif
    @Mutable
    @Shadow
    @Final
    private static ExecutorService IO_POOL;
    //#endif
    @Mutable
    @Shadow
    @Final
    private static ExecutorService BACKGROUND_EXECUTOR;

    //#if MC > 11502
    @Shadow
    private static native void onThreadException(Thread thread, Throwable throwable);
    //#endif

    @Inject(
            //#if MC > 11502
            method = "backgroundExecutor",
            //#else
            //$$ method = "makeBackgroundExecutor",
            //#endif
            at = @At(
                    value = "HEAD"
            )
    )
    private static void onMakeBackgroundExecutor(CallbackInfoReturnable<Executor> ci) {
        if (UtilMixin.magiclib$initMainWorker) {
            return;
        }

        UtilMixin.BACKGROUND_EXECUTOR = ThreadTweaker.replaceForkJoinWorker("Main",
                ThreadTweaker.getGamePriority(), ThreadTweaker.getMainCount());
        MagicLib.getLogger().debug("Main worker replaced");
        UtilMixin.magiclib$initMainWorker = true;
    }

    //#if MC > 11502
    //#if MC > 12003
    //$$ @Inject(
    //$$         method = "nonCriticalIoPool",
    //$$         at = @At(
    //$$                 value = "HEAD"
    //$$         )
    //$$ )
    //$$ private static void onNonCriticalIoPool(CallbackInfoReturnable<Executor> cir) {
    //$$     if (UtilMixin.magiclib$initDownloadWorker) {
    //$$         return;
    //$$     }
    //$$
    //$$     UtilMixin.DOWNLOAD_POOL = ThreadTweaker.replaceThreadWorker("Download-", ThreadTweaker.getIOPriority(),
    //$$             UtilMixin::onThreadException);
    //$$     MagicLib.getLogger().debug("Download worker replaced");
    //$$     UtilMixin.magiclib$initDownloadWorker = true;
    //$$ }
    //#endif

    //#if MC < 11904
    @Inject(
            method = "bootstrapExecutor",
            at = @At(
                    value = "HEAD"
            )
    )
    private static void onBootstrapExecutor(CallbackInfoReturnable<Executor> ci) {
        if (UtilMixin.magiclib$initBootstrapWorker) {
            return;
        }

        UtilMixin.BOOTSTRAP_EXECUTOR = ThreadTweaker.replaceForkJoinWorker("Bootstrap",
                ThreadTweaker.getBootstrapPriority(), ThreadTweaker.getMainCount());
        MagicLib.getLogger().debug("Main worker replaced");
        UtilMixin.magiclib$initBootstrapWorker = true;
    }
    //#endif

    @Inject(
            method = "ioPool",
            at = @At(
                    value = "HEAD"
            )
    )
    private static void onIoPool(CallbackInfoReturnable<Executor> cir) {
        if (UtilMixin.magiclib$initIoWorker) {
            return;
        }

        UtilMixin.IO_POOL = ThreadTweaker.replaceThreadWorker("IO", ThreadTweaker.getIOPriority(),
                UtilMixin::onThreadException);
        MagicLib.getLogger().debug("IO worker replaced");
        UtilMixin.magiclib$initIoWorker = true;
    }
    //#endif
}
