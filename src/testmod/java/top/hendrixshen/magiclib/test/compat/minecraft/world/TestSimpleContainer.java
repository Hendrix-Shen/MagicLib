package top.hendrixshen.magiclib.test.compat.minecraft.world;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class TestSimpleContainer {
    public static void test() {
        SimpleContainer simpleContainer = new SimpleContainer(new ItemStack(Items.AIR));
        simpleContainer.fromTagCompat(new ListTag());
    }
}
