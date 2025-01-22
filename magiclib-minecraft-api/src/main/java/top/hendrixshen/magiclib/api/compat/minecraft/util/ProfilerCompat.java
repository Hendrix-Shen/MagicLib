package top.hendrixshen.magiclib.api.compat.minecraft.util;

import net.minecraft.util.profiling.ProfilerFiller;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12101
//$$ import net.minecraft.util.profiling.Profiler;
//#else
import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.InactiveProfiler;
//#endif
// CHECKSTYLE.ON: ImportOrder

//#if MC < 12103
import top.hendrixshen.magiclib.util.collect.ValueContainer;
//#endif

public interface ProfilerCompat {
    static ProfilerFiller get() {
        //#if MC > 12101
        //$$ return Profiler.get();
        //#else
        return ValueContainer.ofNullable(Minecraft.getInstance())
                .map(Minecraft::getProfiler)
                .orElse(InactiveProfiler.INSTANCE);
        //#endif
    }
}
