/*
 * MIT License
 *
 * Copyright 2022 Steven Cao
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package top.hendrixshen.magiclib.mixin.dev.threadtweak;

import net.minecraft.Util;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.impl.dev.MixinPredicates;
import top.hendrixshen.magiclib.impl.dev.threadtweak.ThreadTweaker;

import java.util.concurrent.Executor;

//#if MC > 12101
//$$ import net.minecraft.TracingExecutor;
//#else
import java.util.concurrent.ExecutorService;
//#endif

/**
 * Reference to <a href="https://github.com/UltimateBoomer/mc-smoothboot/blob/9a519ade89af24aa8b337dfed7d8eb8c0b62ec81/src/main/java/io/github/ultimateboomer/smoothboot/mixin/UtilMixin.java">SmoothBoot<a/>
 */
@Dependencies(require = @Dependency(dependencyType = DependencyType.PREDICATE, predicate = MixinPredicates.TheadTweakPredicate.class))
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
    //#if MC > 12101
    //$$ private static TracingExecutor DOWNLOAD_POOL;
    //#else
    //$$ private static ExecutorService DOWNLOAD_POOL;
    //#endif
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
    //#if MC > 12101
    //$$ private static TracingExecutor IO_POOL;
    //#else
    private static ExecutorService IO_POOL;
    //#endif
    //#endif
    @Mutable
    @Shadow
    @Final
    //#if MC > 12101
    //$$ private static TracingExecutor BACKGROUND_EXECUTOR;
    //#else
    private static ExecutorService BACKGROUND_EXECUTOR;
    //#endif

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
