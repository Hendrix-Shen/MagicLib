package top.hendrixshen.magiclib.mixin.compat.minecraft.world.level.block.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.world.level.block.entity.BlockEntityCompatApi;

//#if MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif

//#if MC > 11502 && MC < 11700
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
//#endif

@Mixin(BlockEntity.class)
public abstract class MixinBlockEntity implements BlockEntityCompatApi {
    //#if MC > 12004
    //$$ @Shadow
    //$$ public abstract void loadWithComponents(CompoundTag compoundTag, HolderLookup.Provider provider);
    //#elseif MC > 11605 || MC < 11600
    //$$ @Shadow
    //$$ public abstract void load(CompoundTag compoundTag);
    //#else
    @Shadow
    public abstract void load(BlockState blockState, CompoundTag compoundTag);

    @Shadow
    public abstract Level getLevel();

    @Shadow
    public abstract BlockPos getBlockPos();
    //#endif

    @Override
    public void loadCompat(
            CompoundTag compoundTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
    ) {
        //#if MC > 12004
        //$$ this.loadWithComponents(compoundTag, provider);
        //#elseif MC > 11605 || MC <= 11502
        //$$ this.load(compoundTag);
        //#else
        this.load(this.getLevel().getBlockState(this.getBlockPos()), compoundTag);
        //#endif
    }
}
