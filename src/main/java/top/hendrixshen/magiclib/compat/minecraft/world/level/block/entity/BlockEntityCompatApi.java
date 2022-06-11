package top.hendrixshen.magiclib.compat.minecraft.world.level.block.entity;

import net.minecraft.nbt.CompoundTag;

public interface BlockEntityCompatApi {
    default void loadCompat(CompoundTag compoundTag) {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11605 && MC > 11502
    //$$ default void load(CompoundTag compoundTag) {
    //$$     this.loadCompat(compoundTag);
    //$$ }
    //#endif
}
