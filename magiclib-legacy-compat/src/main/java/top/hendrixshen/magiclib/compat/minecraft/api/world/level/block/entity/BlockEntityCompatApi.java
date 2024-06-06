package top.hendrixshen.magiclib.compat.minecraft.api.world.level.block.entity;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

//#if MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif

@Deprecated
@ApiStatus.ScheduledForRemoval
public interface BlockEntityCompatApi {
    default void loadCompat(
            CompoundTag compoundTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider holder
            //#endif
    ) {
        throw new UnImplCompatApiException();
    }

    //#if MC > 11502 && MC < 11700
    default void load(
            CompoundTag compoundTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider holder
            //#endif
    ) {
        this.loadCompat(
                compoundTag
                //#if MC > 12004
                //$$ , holder
                //#endif
        );
    }
    //#endif
}
