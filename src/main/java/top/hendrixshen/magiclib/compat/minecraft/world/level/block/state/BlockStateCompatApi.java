package top.hendrixshen.magiclib.compat.minecraft.world.level.block.state;

import net.minecraft.world.level.block.Block;

public interface BlockStateCompatApi {
    default boolean isCompat(Block block) {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11502
    //$$ default boolean is(Block block) {
    //$$     return this.isCompat(block);
    //$$ }
    //#endif
}
