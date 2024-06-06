package top.hendrixshen.magiclib.impl.compat.minecraft.world.level.block;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.level.block.BlockEntityCompat;

import java.util.Objects;

//#if MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif

public class BlockEntityCompatImpl extends AbstractCompat<BlockEntity> implements BlockEntityCompat {
    public BlockEntityCompatImpl(@NotNull BlockEntity type) {
        super(type);
    }

    @Override
    public void load(
            @NotNull CompoundTag compoundTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
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
