package top.hendrixshen.magiclib.compat.minecraft.api.world;

import net.minecraft.nbt.ListTag;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

//#if MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif

@Deprecated
@ApiStatus.ScheduledForRemoval
public interface SimpleContainerCompatApi {
    default void fromTagCompat(
            ListTag listTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
    ) {
        throw new UnImplCompatApiException();
    }
}
