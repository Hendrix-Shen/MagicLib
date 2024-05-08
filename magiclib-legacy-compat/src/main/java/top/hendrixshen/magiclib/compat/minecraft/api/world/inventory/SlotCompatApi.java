package top.hendrixshen.magiclib.compat.minecraft.api.world.inventory;

import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

public interface SlotCompatApi {
    default int getContainerSlotCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11605
    default int getContainerSlot() {
        return this.getContainerSlotCompat();
    }
    //#endif
}
