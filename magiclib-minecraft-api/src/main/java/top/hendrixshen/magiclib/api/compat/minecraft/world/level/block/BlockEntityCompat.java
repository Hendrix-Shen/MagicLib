package top.hendrixshen.magiclib.api.compat.minecraft.world.level.block;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.level.block.BlockEntityCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

//#if MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif

public interface BlockEntityCompat extends Provider<BlockEntity> {
    static @NotNull BlockEntityCompat of(BlockEntity blockEntity) {
        return new BlockEntityCompatImpl(blockEntity);
    }

    void load(
            @NotNull CompoundTag compoundTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
    );
}
