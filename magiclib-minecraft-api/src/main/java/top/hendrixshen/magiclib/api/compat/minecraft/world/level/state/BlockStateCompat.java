package top.hendrixshen.magiclib.api.compat.minecraft.world.level.state;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.level.state.BlockStateCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface BlockStateCompat extends Provider<BlockState> {
    static @NotNull BlockStateCompat of(@NotNull BlockState blockState) {
        return new BlockStateCompatImpl(blockState);
    }

    boolean is(Block block);
}
