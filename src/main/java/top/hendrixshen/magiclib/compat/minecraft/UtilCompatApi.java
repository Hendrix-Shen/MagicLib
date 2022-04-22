package top.hendrixshen.magiclib.compat.minecraft;

import net.minecraft.Util;

import java.util.UUID;

public interface UtilCompatApi {
    //#if MC >= 11605
    UUID NIL_UUID = Util.NIL_UUID;
    //#else
    //$$ UUID NIL_UUID = new UUID(0L, 0L);
    //#endif
}
