package top.hendrixshen.magiclib.compat.minecraft.api.world;

import net.minecraft.nbt.ListTag;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

public interface SimpleContainerCompatApi {
    // fuck remap
    default void fromTagCompat(ListTag listTag) {
        throw new UnImplCompatApiException();
    }
    //#if MC < 11502
    //$$ // fuck remap
    //$$ // it will be remapped to intermediary name....
    //$$ ////$$ void fromTagCompat(ListTag listTag) {
    //$$ ////$$     this.fromTagCompat(listTag);
    //$$ ////$$ }
    //#endif
}
