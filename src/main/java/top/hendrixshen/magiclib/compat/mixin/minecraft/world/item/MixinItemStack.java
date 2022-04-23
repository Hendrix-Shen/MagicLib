package top.hendrixshen.magiclib.compat.mixin.minecraft.world.item;

import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import top.hendrixshen.magiclib.compat.minecraft.world.item.ItemStackCompatApi;

//#if MC <= 11605
//$$ import org.spongepowered.asm.mixin.Shadow;
//$$ import net.minecraft.world.item.Item;
//#endif

@Mixin(ItemStack.class)
public abstract class MixinItemStack implements ItemStackCompatApi {

    //#if MC <= 11605
    //$$ @Shadow
    //$$ public abstract Item getItem();

    //$$ @Override
    //$$ public boolean is(Item item) {
    //$$     return this.getItem() == item;
    //$$ }
    //#endif
}
