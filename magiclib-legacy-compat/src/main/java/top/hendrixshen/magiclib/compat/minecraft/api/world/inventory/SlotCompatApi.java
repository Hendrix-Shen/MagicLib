package top.hendrixshen.magiclib.compat.minecraft.api.world.inventory;

import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
public interface SlotCompatApi {
    default int getContainerSlotCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC < 11700
    default int getContainerSlot() {
        return this.getContainerSlotCompat();
    }
    //#endif
}
