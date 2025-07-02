package top.hendrixshen.magiclib.mixin.dev.threadtweak;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.jetbrains.annotations.NotNull;

import net.minecraft.util.datafix.DataFixers;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import top.hendrixshen.magiclib.impl.dev.threadtweak.ThreadTweaker;

import java.util.concurrent.ThreadFactory;

// CHECKSTYLE.OFF: JavadocStyle
/**
 * <li>mc1.14 ~ mc1.19.3: subproject 1.16.5 (main project) [dummy]</li>
 * <li>mc1.19.4 ~ mc1.21: subproject 1.19.4        &lt;--------</li>
 * <li>mc1.21.1+        : subproject 1.21.1 [dummy]</li>
 */
// CHECKSTYLE.ON: JavadocStyle
@Mixin(value = DataFixers.class, remap = false)
public abstract class DataFixersMixin {
    @Redirect(
            method = "createFixerUpper",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/google/common/util/concurrent/ThreadFactoryBuilder;build()Ljava/util/concurrent/ThreadFactory;"
            )
    )
    private static @NotNull ThreadFactory onBuildThread(@NotNull ThreadFactoryBuilder builder) {
        builder.setPriority(ThreadTweaker.getBootstrapPriority());
        return builder.build();
    }
}
