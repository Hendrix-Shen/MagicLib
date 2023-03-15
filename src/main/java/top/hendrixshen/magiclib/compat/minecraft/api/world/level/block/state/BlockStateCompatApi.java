package top.hendrixshen.magiclib.compat.minecraft.api.world.level.block.state;

import net.minecraft.world.level.block.Block;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

public interface BlockStateCompatApi {
    default boolean isCompat(Block block) {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11502
    //$$ default boolean is(Block block) {
    //$$     return this.isCompat(block);
    //$$ }
    //#endif
}
