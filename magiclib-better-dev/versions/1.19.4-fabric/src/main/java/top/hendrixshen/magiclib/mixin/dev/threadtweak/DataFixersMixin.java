package top.hendrixshen.magiclib.mixin.dev.threadtweak;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import net.minecraft.util.datafix.DataFixers;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import top.hendrixshen.magiclib.impl.dev.threadtweak.ThreadTweaker;

import java.util.concurrent.ThreadFactory;

@Mixin(value = DataFixers.class, remap = false)
public class DataFixersMixin {
    @Redirect(
            method = "createFixerUpper",
            at = @At(
                    value = "INVOKE",
                    target= "Lcom/google/common/util/concurrent/ThreadFactoryBuilder;build()Ljava/util/concurrent/ThreadFactory;"
            )
    )
    private static @NotNull ThreadFactory onBuildThread(@NotNull ThreadFactoryBuilder builder) {
        builder.setPriority(ThreadTweaker.getBootstrapPriority());
        return builder.build();
    }
}
