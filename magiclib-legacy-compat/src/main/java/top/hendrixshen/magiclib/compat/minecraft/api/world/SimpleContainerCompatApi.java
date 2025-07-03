package top.hendrixshen.magiclib.compat.minecraft.api.world;

import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

//#if MC >= 12106
//$$ import net.minecraft.world.item.ItemStack;
//$$ import net.minecraft.world.level.storage.ValueInput;
//#else
import net.minecraft.nbt.ListTag;
//#endif

//#if MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif

@Deprecated
@ApiStatus.ScheduledForRemoval
public interface SimpleContainerCompatApi {
    default void fromTagCompat(
            //#if MC >= 12106
            //$$ ValueInput.TypedInputList<ItemStack> typedInputList
            //#else
            ListTag listTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
            //#endif
    ) {
        throw new UnImplCompatApiException();
    }
}
