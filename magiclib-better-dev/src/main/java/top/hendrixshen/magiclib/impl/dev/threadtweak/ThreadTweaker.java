package top.hendrixshen.magiclib.impl.dev.threadtweak;

import net.minecraft.ReportedException;
import net.minecraft.server.Bootstrap;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.MagicLibProperties;
import top.hendrixshen.magiclib.mixin.dev.accessor.UtilAccessor;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadTweaker {
    public static class Default {
        public static final int bootstrapCount = 1;
        public static final int mainCount = MagicLibProperties.getMaxBackgroundThreads();
        public static final int bootstrapPriority = 1;
        public static final int gamePriority = 5;
        public static final int integratedServerPriority = 5;
        public static final int ioPriority = 1;
        public static final int mainPriority = 1;
    }

    public static @NotNull ExecutorService replaceForkJoinWorker(String name, int priority, int counter) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        return new ForkJoinPool(Mth.clamp(counter, 1, 0x7fff), (forkJoinPool) -> {
            //#if MC > 11502
            //$$ String workerName = "Worker-" + name + "-" + atomicInteger.getAndIncrement();
            //#else
            String workerName = "Server-Worker-" + name + "-" + atomicInteger.getAndIncrement();
            //#endif
            MagicLib.getLogger().debug("Initialized " + workerName);
            ForkJoinWorkerThread forkJoinWorkerThread = new LoggingForkJoinWorkerThread(forkJoinPool,
                    UtilAccessor.magiclib$getLogger());
            forkJoinWorkerThread.setPriority(priority);
            forkJoinWorkerThread.setName(workerName);
            return forkJoinWorkerThread;
        }, (thread, throwable) -> {
            if (throwable instanceof CompletionException) {
                throwable = throwable.getCause();
            }

            if (throwable instanceof ReportedException) {
                Bootstrap.realStdoutPrintln(((ReportedException) throwable).getReport().getFriendlyReport());
                System.exit(-1);
            }

            UtilAccessor.magiclib$getLogger().error(String.format("Caught exception in thread %s", thread), throwable);
        }, true);
    }

    public static @NotNull ExecutorService replaceThreadWorker(
            String name, int priority, Thread.UncaughtExceptionHandler uncaughtExceptionHandler) {
        AtomicInteger atomicInteger = new AtomicInteger(1);
        return Executors.newCachedThreadPool((runnable) -> {
            String workerName = name + "-" + atomicInteger.getAndIncrement();
            MagicLib.getLogger().debug("Initialized " + workerName);
            Thread thread = new Thread(runnable);
            thread.setName(workerName);
            thread.setDaemon(true);
            thread.setPriority(priority);
            thread.setUncaughtExceptionHandler(uncaughtExceptionHandler);
            return thread;
        });
    }

    public static int getBootstrapCount() {
        try {
            int i = Integer.parseInt(MagicLibProperties.DEV_QOL_THREAD_TWEAK_COUNT_BOOTSTRAP.getStringValue());
            return Math.max(i, 1);
        } catch (NumberFormatException e) {
            return Default.bootstrapCount;
        }
    }

    public static int getMainCount() {
        try {
            int i = Integer.parseInt(MagicLibProperties.DEV_QOL_THREAD_TWEAK_COUNT_MAIN.getStringValue());
            return Math.max(i, 1);
        } catch (NumberFormatException e) {
            return Default.mainCount;
        }
    }

    public static int getBootstrapPriority() {
        try {
            int i = Integer.parseInt(MagicLibProperties.DEV_QOL_THREAD_TWEAK_PRIORITY_BOOTSTRAP.getStringValue());
            return Mth.clamp(i, 1, 10);
        } catch (NumberFormatException e) {
            return Default.bootstrapPriority;
        }
    }

    public static int getGamePriority() {
        try {
            int i = Integer.parseInt(MagicLibProperties.DEV_QOL_THREAD_TWEAK_PRIORITY_GAME.getStringValue());
            return Mth.clamp(i, 1, 10);
        } catch (NumberFormatException e) {
            return Default.gamePriority;
        }
    }

    public static int getIntegratedServerPriority() {
        try {
            int i = Integer.parseInt(MagicLibProperties.DEV_QOL_THREAD_TWEAK_PRIORITY_INTEGRATED_SERVER
                    .getStringValue());
            return Mth.clamp(i, 1, 10);
        } catch (NumberFormatException e) {
            return Default.integratedServerPriority;
        }
    }

    public static int getIOPriority() {
        try {
            int i = Integer.parseInt(MagicLibProperties.DEV_QOL_THREAD_TWEAK_PRIORITY_IO.getStringValue());
            return Mth.clamp(i, 1, 10);
        } catch (NumberFormatException e) {
            return Default.ioPriority;
        }
    }

    public static int getMainPriority() {
        try {
            int i = Integer.parseInt(MagicLibProperties.DEV_QOL_THREAD_TWEAK_PRIORITY_MAIN.getStringValue());
            return Mth.clamp(i, 1, 10);
        } catch (NumberFormatException e) {
            return Default.mainPriority;
        }
    }
}
