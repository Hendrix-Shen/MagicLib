package top.hendrixshen.magiclib.compat.mixin.minecraft.world.level.block.state;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Mixin(BlockState.class)
public abstract class MixinBlockState {
    @Shadow
    public abstract Block getBlock();

    @Remap("method_26204")
    public Block getBlockCompat() {
        return this.getBlock();
    }

    @Remap("method_27852")
    public boolean is(Block block) {
        return this.getBlock() == block;
    }

}
