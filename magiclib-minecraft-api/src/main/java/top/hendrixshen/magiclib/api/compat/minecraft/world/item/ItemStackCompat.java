package top.hendrixshen.magiclib.api.compat.minecraft.world.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.item.ItemStackCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

//#if MC > 11904
//$$ import java.util.Objects;
//#endif

public interface ItemStackCompat extends Provider<ItemStack> {
    static @NotNull ItemStackCompat of(@NotNull ItemStack itemStack) {
        return new ItemStackCompatImpl(itemStack);
    }

    static boolean tagMatches(ItemStack itemStack, ItemStack itemStack2) {
        //#if MC > 11904
        //$$ if (itemStack.isEmpty() && itemStack2.isEmpty()) {
        //$$     return true;
        //$$ }
        //$$
        //$$ if (itemStack.isEmpty() || itemStack2.isEmpty()) {
        //$$     return false;
        //$$ }
        //$$
        //#if MC > 12004
        //$$ return Objects.equals(itemStack.getComponents(), itemStack2.getComponents());
        //#else
        //$$ if (itemStack.getTag() == null && itemStack2.getTag() != null) {
        //$$     return false;
        //$$ }
        //$$
        //$$ return Objects.equals(itemStack.getTag(), itemStack2.getTag());
        //#endif
        //#else
        return ItemStack.tagMatches(itemStack, itemStack2);
        //#endif
    }

    static boolean isSame(ItemStack itemStack, ItemStack itemStack2) {
        return ItemStack.isSame(itemStack, itemStack2);
    }

    static boolean isSameIgnoreDurability(ItemStack itemStack, ItemStack itemStack2) {
        //#if MC > 11902
        //$$ if (itemStack == itemStack2) {
        //$$     return true;
        //$$ }
        //$$
        //$$ if (itemStack.isEmpty() && itemStack2.isEmpty()) {
        //$$     return false;
        //$$ }
        //$$
        //$$ if (itemStack.isDamageableItem()) {
        //$$     return !itemStack2.isEmpty() && itemStack.is(itemStack2.getItem());
        //$$ } else {
        //$$     return ItemStackCompat.isSame(itemStack, itemStack2);
        //$$ }
        //#else
        return ItemStack.isSameIgnoreDurability(itemStack, itemStack2);
        //#endif
    }

    static boolean isSameItemSameTags(@NotNull ItemStack itemStack, @NotNull ItemStack itemStack2) {
        //#if MC > 11605
        //$$ return ItemStack.isSameItemSameTags(itemStack, itemStack2);
        //#else
        return itemStack.getItem() == itemStack2.getItem() && ItemStackCompat.tagMatches(itemStack, itemStack2);
        //#endif
    }

    static boolean isSameItemSameTagsIgnoreDurability(@NotNull ItemStack itemStack, @NotNull ItemStack itemStack2) {
        return ItemStackCompat.isSameIgnoreDurability(itemStack, itemStack2) &&
                ItemStackCompat.tagMatches(itemStack, itemStack2);
    }

    boolean is(Item item);
}
