package top.hendrixshen.magiclib.compat.minecraft.api.world.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

@Deprecated
@ApiStatus.ScheduledForRemoval
public interface ItemStackCompatApi {
    static boolean isSameItemSameTags(ItemStack itemStack, ItemStack itemStack2) {
        //#if MC > 11605
        //$$ return ItemStack.isSameItemSameTags(itemStack, itemStack2);
        //#else
        return itemStack.is(itemStack2.getItem()) && ItemStack.tagMatches(itemStack, itemStack2);
        //#endif
    }

    //#if MC < 11700
    default boolean is(Item item) {
        return this.isCompat(item);
    }
    //#endif

    default boolean isCompat(Item item) {
        throw new UnImplCompatApiException();
    }
}
