package top.hendrixshen.magiclib.test.compat.minecraft.world.level.block.state;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class TestBlockState {
    public static void test() {
        BlockState blockState = Blocks.AIR.defaultBlockState();
        blockState.isCompat(Blocks.AIR);
        blockState.is(Blocks.AIR);
    }
}
