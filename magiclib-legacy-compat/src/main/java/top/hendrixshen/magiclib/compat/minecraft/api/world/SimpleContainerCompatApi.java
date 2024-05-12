package top.hendrixshen.magiclib.compat.minecraft.api.world;

import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
public interface SimpleContainerCompatApi {
    default void fromTagCompat(ListTag listTag) {
        throw new UnImplCompatApiException();
    }
}
