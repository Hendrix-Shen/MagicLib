package top.hendrixshen.magiclib.impl.compat.minecraft.world.level.state;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.level.state.BlockStateCompat;

public class BlockStateCompatImpl extends AbstractCompat<BlockState> implements BlockStateCompat {
    public BlockStateCompatImpl(@NotNull BlockState type) {
        super(type);
    }

    public boolean is(Block block) {
        //#if MC > 11502
        //$$ return this.get().is(block);
        //#else
        return this.get().getBlock() == block;
        //#endif
    }

}
