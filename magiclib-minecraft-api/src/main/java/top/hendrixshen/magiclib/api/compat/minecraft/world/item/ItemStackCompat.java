package top.hendrixshen.magiclib.api.compat.minecraft.world.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.item.ItemStackCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface ItemStackCompat extends Provider<ItemStack> {
    static @NotNull ItemStackCompat of(@NotNull ItemStack itemStack) {
        return new ItemStackCompatImpl(itemStack);
    }

    boolean is(Item item);
}
