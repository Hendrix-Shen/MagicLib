package top.hendrixshen.magiclib.api.compat.minecraft.world.level.block;

import org.jetbrains.annotations.NotNull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.compat.minecraft.world.level.block.BlockEntityCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface BlockEntityCompat extends Provider<BlockEntity> {
    static @NotNull BlockEntityCompat of(BlockEntity blockEntity) {
        return new BlockEntityCompatImpl(blockEntity);
    }

    void load(
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            @NotNull CompoundTag compoundTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    );
}
