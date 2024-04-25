package top.hendrixshen.magiclib.impl.dev.threadtweak;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

//#if MC > 11701
import org.slf4j.Logger;
//#else
//$$ import org.apache.logging.log4j.Logger;
//#endif

public class LoggingForkJoinWorkerThread extends ForkJoinWorkerThread {
    private final Logger logger;

    public LoggingForkJoinWorkerThread(ForkJoinPool pool, Logger logger) {
        super(pool);
        this.logger = logger;
    }

    @Override
    protected void onTermination(Throwable throwable) {
        if (throwable != null) {
            logger.warn("{} died", this.getName(), throwable);
        } else {
            logger.debug("{} shutdown", this.getName());
        }

        super.onTermination(throwable);
    }
}
