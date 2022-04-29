package top.hendrixshen.magiclib.test.compat.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;

public class TestBlockEntity {
    public static void test() {
        //#if MC > 11605
        ComparatorBlockEntity blockEntity = new ComparatorBlockEntity(BlockPos.ZERO, Blocks.COMPARATOR.defaultBlockState());
        //#else
        //$$ ComparatorBlockEntity blockEntity = new ComparatorBlockEntity();
        //#endif
        //TODO
//        CompoundTag component0 = blockEntity.saveWithFullMetadata();
//        CompoundTag component1 = blockEntity.saveWithId();
//        CompoundTag component2 = blockEntity.saveWithoutMetadata();
        // Crash in low version, because getLeve() == null
        // blockEntity.loadCompat(new CompoundTag());
        // blockEntity.load(new CompoundTag());
    }
}
