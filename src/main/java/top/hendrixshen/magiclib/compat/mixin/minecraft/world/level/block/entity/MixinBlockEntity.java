package top.hendrixshen.magiclib.compat.mixin.minecraft.world.level.block.entity;


import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.world.level.block.entity.BlockEntityCompatApi;

//#if MC <= 11605
//$$ import net.minecraft.core.BlockPos;
//$$ import net.minecraft.nbt.CompoundTag;
//$$ import net.minecraft.world.level.Level;
//$$ import net.minecraft.world.level.block.state.BlockState;
//$$ import org.spongepowered.asm.mixin.Shadow;
//#endif

@Mixin(BlockEntity.class)
public abstract class MixinBlockEntity implements BlockEntityCompatApi {

    //#if MC <= 11605 && MC > 11502
    //$$ @Shadow
    //$$ public abstract void load(BlockState blockState, CompoundTag compoundTag);

    //$$ @Shadow
    //$$ public abstract Level getLevel();

    //$$ @Shadow
    //$$ public abstract BlockPos getBlockPos();

    //$$ @Override
    //$$ public void load(CompoundTag compoundTag) {
    //$$     this.load(this.getLevel().getBlockState(this.getBlockPos()), compoundTag);
    //$$ }
    //#endif
}
