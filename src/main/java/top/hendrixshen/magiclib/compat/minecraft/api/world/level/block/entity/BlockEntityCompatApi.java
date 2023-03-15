package top.hendrixshen.magiclib.compat.minecraft.api.world.level.block.entity;

import net.minecraft.nbt.CompoundTag;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

public interface BlockEntityCompatApi {
    default void loadCompat(CompoundTag compoundTag) {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11605 && MC > 11502
    //$$ default void load(CompoundTag compoundTag) {
    //$$     this.loadCompat(compoundTag);
    //$$ }
    //#endif
}
