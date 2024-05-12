package top.hendrixshen.magiclib.compat.minecraft.api;

import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.api.compat.minecraft.UtilCompat;

import java.util.UUID;

@Deprecated
@ApiStatus.ScheduledForRemoval
public interface UtilCompatApi {
    UUID NIL_UUID = UtilCompat.NIL_UUID;
}
