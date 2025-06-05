package top.hendrixshen.magiclib.util.runner;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.concurrent.atomic.AtomicBoolean;

public class SingleRunner implements Runnable {
    @NotNull
    private final Runnable runnable;
    private final AtomicBoolean hasRun = new AtomicBoolean(false);

    public SingleRunner(@NotNull Runnable runnable) {
        this.runnable = Objects.requireNonNull(runnable);
    }

    @Override
    public void run() {
        if (this.hasRun.compareAndSet(false, true)) {
            this.runnable.run();
        }
    }
}
