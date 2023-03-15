package top.hendrixshen.magiclib.compat.minecraft.mixin.world;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.api.world.SimpleContainerCompatApi;

//#if MC <= 11502
//$$ import net.minecraft.world.item.ItemStack;
//#endif

@Mixin(SimpleContainer.class)
public abstract class MixinSimpleContainer implements SimpleContainerCompatApi {
    //#if MC > 11502
    @Shadow
    public abstract void fromTag(ListTag listTag);
    //#else
    //$$ @Shadow
    //$$ public abstract ItemStack addItem(ItemStack itemStack);
    //$$
    //#endif

    @Override
    public void fromTagCompat(ListTag listTag) {
        //#if MC > 11502
        this.fromTag(listTag);
        //#else
        //$$ for (int i = 0; i < listTag.size(); ++i) {
        //$$     ItemStack itemStack = ItemStack.of(listTag.getCompound(i));
        //$$     if (!itemStack.isEmpty()) {
        //$$         this.addItem(itemStack);
        //$$     }
        //$$ }
        //#endif
    }
}