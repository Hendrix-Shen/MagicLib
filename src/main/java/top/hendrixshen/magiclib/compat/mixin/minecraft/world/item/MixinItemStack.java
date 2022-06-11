package top.hendrixshen.magiclib.compat.mixin.minecraft.world.item;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.world.item.ItemStackCompatApi;

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements ItemStackCompatApi {
    //#if MC > 11605
    @Shadow
    public abstract boolean is(Item item);
    //#else
    //$$ @Shadow
    //$$ public abstract Item getItem();
    //#endif

    @Override
    public boolean isCompat(Item item) {
        //#if MC > 11605
        return this.is(item);
        //#else
        //$$     return this.getItem() == item;
        //#endif
    }
}
