package top.hendrixshen.magiclib.compat.minecraft.world;

import net.minecraft.nbt.ListTag;

public interface SimpleContainerCompatApi {
    // fuck remap
    default void fromTagCompat(ListTag listTag) {
        throw new UnsupportedOperationException();
    }
    //#if MC < 11502
    //$$ // fuck remap
    //$$ // it will be remapped to intermediary name....
    //$$ ////$$ void fromTagCompat(ListTag listTag) {
    //$$ ////$$     this.fromTagCompat(listTag);
    //$$ ////$$ }
    //#endif
}
