package top.hendrixshen.magiclib.compat.minecraft.mixin.world.level.block.state;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.api.world.level.block.state.BlockStateCompatApi;
import top.hendrixshen.magiclib.util.MiscUtil;

@Mixin(BlockState.class)
public class MixinBlockState implements BlockStateCompatApi {
    @Override
    public boolean isCompat(Block block) {
        BlockState thisObj = MiscUtil.cast(this);
        //#if MC > 11502
        return thisObj.is(block);
        //#else
        //$$ return thisObj.getBlock() == block;
        //#endif
    }
}
