package top.hendrixshen.magiclib.api.compat.minecraft.util;

import net.minecraft.client.Minecraft;
import net.minecraft.util.profiling.InactiveProfiler;
import net.minecraft.util.profiling.ProfilerFiller;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

//#if MC > 12101
//$$ import net.minecraft.util.profiling.Profiler;
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
