package top.hendrixshen.magiclib.compat.minecraft.world.inventory;

public interface SlotCompatApi {
    default int getContainerSlotCompat() {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11605
    //$$ default int getContainerSlot() {
    //$$     return this.getContainerSlotCompat();
    //$$ }
    //#endif
}
