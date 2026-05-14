package top.hendrixshen.magiclib.mixin.compat.minecraft.world.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.world.item.ItemStackCompatApi;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements ItemStackCompatApi {
    @Shadow
    public abstract Item getItem();

    @Override
    public boolean isCompat(Item item) {
        return this.getItem() == item;
    }
}
