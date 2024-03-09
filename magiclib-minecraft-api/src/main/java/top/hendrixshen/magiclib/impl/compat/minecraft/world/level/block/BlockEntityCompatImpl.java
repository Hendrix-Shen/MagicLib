package top.hendrixshen.magiclib.impl.compat.minecraft.world.level.block;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.level.block.BlockEntityCompat;

public class BlockEntityCompatImpl extends AbstractCompat<BlockEntity> implements BlockEntityCompat {
    public BlockEntityCompatImpl(@NotNull BlockEntity type) {
        super(type);
    }

    @Override
    public void load(@NotNull CompoundTag compoundTag) {
        //#if MC > 11605 || MC < 11600
        this.get().load(compoundTag);
        //#else
        //$$ this.get().load(this.get().getLevel().getBlockState(this.get().getBlockPos()), compoundTag);
        //#endif
    }
}
