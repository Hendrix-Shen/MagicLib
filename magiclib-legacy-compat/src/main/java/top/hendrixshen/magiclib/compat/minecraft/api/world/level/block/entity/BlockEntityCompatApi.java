package top.hendrixshen.magiclib.compat.minecraft.api.world.level.block.entity;

import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
public interface BlockEntityCompatApi {
    default void loadCompat(CompoundTag compoundTag) {
        throw new UnImplCompatApiException();
    }

    //#if MC > 11502 && MC < 11700
    default void load(CompoundTag compoundTag) {
        this.loadCompat(compoundTag);
    }
    //#endif
}
