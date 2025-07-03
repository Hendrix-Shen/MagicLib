package top.hendrixshen.magiclib.mixin.compat.minecraft.world.level.block.entity;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.compat.minecraft.world.level.block.BlockEntityCompat;
import top.hendrixshen.magiclib.compat.minecraft.api.world.level.block.entity.BlockEntityCompatApi;

//#if MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif

@Mixin(BlockEntity.class)
public abstract class MixinBlockEntity implements BlockEntityCompatApi {
    @Override
    public void loadCompat(
            CompoundTag compoundTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
    ) {
        BlockEntityCompat blockEntityCompat = BlockEntityCompat.of((BlockEntity) (Object) this);
        blockEntityCompat.load(
                compoundTag
                //#if MC > 12004
                //$$ , provider
                //#endif
        );
    }
}
