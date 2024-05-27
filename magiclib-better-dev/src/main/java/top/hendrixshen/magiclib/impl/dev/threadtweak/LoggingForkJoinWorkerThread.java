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

package top.hendrixshen.magiclib.impl.dev.threadtweak;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinWorkerThread;

//#if MC > 11701
//$$ import org.slf4j.Logger;
//#else
import org.apache.logging.log4j.Logger;
//#endif

/**
 * Reference to <a href="https://github.com/UltimateBoomer/mc-smoothboot/blob/ce5c0482e51698a424ea3d09e994d4b71a7c71b6/src/main/java/io//github/ultimateboomer/smoothboot/util/LoggingForkJoinWorkerThread.java">SmoothBoot<a/>
 */
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
