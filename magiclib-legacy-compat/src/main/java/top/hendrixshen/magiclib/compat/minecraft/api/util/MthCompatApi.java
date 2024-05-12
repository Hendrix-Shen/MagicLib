package top.hendrixshen.magiclib.compat.minecraft.api.util;

import net.minecraft.util.Mth;
import org.jetbrains.annotations.ApiStatus;

@Deprecated
@ApiStatus.ScheduledForRemoval
public interface MthCompatApi {
    static float fastInvCubeRoot(float f) {
        //#if MC > 11404
        return Mth.fastInvCubeRoot(f);
        //#else
        //$$ int i = Float.floatToIntBits(f);
        //$$ i = 1419967116 - i / 3;
        //$$ float g = Float.intBitsToFloat(i);
        //$$ g = 0.6666667F * g + 1.0F / (3.0F * g * g * f);
        //$$ g = 0.6666667F * g + 1.0F / (3.0F * g * g * f);
        //$$ return g;
        //#endif
    }
}
