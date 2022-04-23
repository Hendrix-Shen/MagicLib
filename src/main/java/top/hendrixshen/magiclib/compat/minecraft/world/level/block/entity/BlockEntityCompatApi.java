package top.hendrixshen.magiclib.compat.minecraft.world.level.block.entity;

//#if MC <= 11605
//$$ import net.minecraft.nbt.CompoundTag;
//#endif

public interface BlockEntityCompatApi {
    //#if MC <= 11605
    //$$ default void load(CompoundTag compoundTag) {
    //$$     throw new UnsupportedOperationException();
    //$$ }
    //#endif
}
