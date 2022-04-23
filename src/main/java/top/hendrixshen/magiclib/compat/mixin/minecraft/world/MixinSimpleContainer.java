package top.hendrixshen.magiclib.compat.mixin.minecraft.world;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.minecraft.world.SimpleContainerCompatApi;


@Mixin(SimpleContainer.class)
public abstract class MixinSimpleContainer implements SimpleContainerCompatApi {
    @Shadow
    public abstract ItemStack addItem(ItemStack itemStack);

    @Override
    public void fromTagCompat(ListTag listTag) {
        for (int i = 0; i < listTag.size(); ++i) {
            ItemStack itemStack = ItemStack.of(listTag.getCompound(i));
            if (!itemStack.isEmpty()) {
                this.addItem(itemStack);
            }
        }
    }
}