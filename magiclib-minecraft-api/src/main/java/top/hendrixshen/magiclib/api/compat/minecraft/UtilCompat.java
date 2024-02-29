package top.hendrixshen.magiclib.api.compat.minecraft;

import java.util.UUID;

//#if MC > 11502
//$$ import net.minecraft.Util;
//#endif

public interface UtilCompat {
    //#if MC > 11502
    //$$ UUID NIL_UUID = Util.NIL_UUID;
    //#else
    UUID NIL_UUID = new UUID(0L, 0L);
    //#endif
}
