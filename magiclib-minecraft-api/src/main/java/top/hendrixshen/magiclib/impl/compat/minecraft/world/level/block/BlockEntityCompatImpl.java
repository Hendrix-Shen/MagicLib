package top.hendrixshen.magiclib.impl.compat.minecraft.world.level.block;

import org.jetbrains.annotations.NotNull;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.level.block.BlockEntityCompat;

//#if 11700 > MC && MC > 11502
import java.util.Objects;
//#endif

public class BlockEntityCompatImpl extends AbstractCompat<BlockEntity> implements BlockEntityCompat {
    public BlockEntityCompatImpl(@NotNull BlockEntity type) {
        super(type);
    }

    @Override
    public void load(
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            @NotNull CompoundTag compoundTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    ) {
        //#if MC > 12004
        //$$ this.get().loadWithComponents(compoundTag, provider);
        //#elseif MC > 11605 || MC < 11600
        //$$ this.get().load(compoundTag);
        //#else
        BlockEntity blockEntity = this.get();
        blockEntity.load(Objects.requireNonNull(blockEntity.getLevel())
                .getBlockState(this.get().getBlockPos()), compoundTag);
        //#endif
    }
}
