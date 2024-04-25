package top.hendrixshen.magiclib.impl.compat.minecraft.world.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.item.ItemStackCompat;

public class ItemStackCompatImpl extends AbstractCompat<ItemStack> implements ItemStackCompat {
    public ItemStackCompatImpl(@NotNull ItemStack type) {
        super(type);
    }

    @Override
    public boolean is(Item item) {
        //#if MC > 11605
        return this.get().is(item);
        //#else
        //$$ return this.get().getItem() == item;
        //#endif
    }
}
