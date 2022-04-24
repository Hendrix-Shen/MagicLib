package top.hendrixshen.magiclib.test.compat.minecraft.world.item;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import top.hendrixshen.magiclib.compat.minecraft.world.item.ItemStackCompatApi;

public class TestItemStack {
    public static void test() {
        ItemStackCompatApi.isSameItemSameTags(new ItemStack(Items.AIR), new ItemStack(Items.AIR));
        ItemStack itemStack = new ItemStack(Items.AIR);
        itemStack.isCompat(Items.AIR);
        itemStack.is(Items.AIR);
    }
}
