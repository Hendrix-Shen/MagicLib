/*
 * Copyright 2016 FabricMC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package top.hendrixshen.magiclib.util.fabric;

import org.jetbrains.annotations.NotNull;

import java.io.UncheckedIOException;
import java.util.concurrent.CompletionException;
import java.util.concurrent.ExecutionException;

public class ExceptionUtil {
    public static @NotNull RuntimeException wrap(Throwable exc) {
        if (exc instanceof RuntimeException) {
            return (RuntimeException) exc;
        }

        exc = ExceptionUtil.unwrap(exc);

        if (exc instanceof RuntimeException) {
            return (RuntimeException) exc;
        }

        return new WrappedException(exc);
    }

    private static Throwable unwrap(Throwable exc) {
        if (exc instanceof WrappedException
                || exc instanceof UncheckedIOException
                || exc instanceof ExecutionException
                || exc instanceof CompletionException) {
            Throwable ret = exc.getCause();

            if (ret != null) {
                return ExceptionUtil.unwrap(ret);
            }
        }

        return exc;
    }

    @SuppressWarnings("serial")
    public static final class WrappedException extends RuntimeException {
        public WrappedException(Throwable cause) {
            super(cause);
        }
    }
}
