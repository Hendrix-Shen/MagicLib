package top.hendrixshen.magiclib.compat.minecraft.world.item;

//#if MC <= 11605
//$$ import net.minecraft.world.item.Item;
//#endif

import net.minecraft.world.item.ItemStack;

public interface ItemStackCompatApi {
    //#if MC <= 11605
    //$$ default boolean is(Item item) {
    //$$     throw new UnsupportedOperationException();
    //$$ }
    //#endif
    static boolean isSameItemSameTags(ItemStack itemStack, ItemStack itemStack2) {
        //#if MC > 11605
        return ItemStack.isSameItemSameTags(itemStack, itemStack2);
        //#else
        //$$ return itemStack.is(itemStack2.getItem()) && ItemStack.tagMatches(itemStack, itemStack2);
        //#endif
    }
}
