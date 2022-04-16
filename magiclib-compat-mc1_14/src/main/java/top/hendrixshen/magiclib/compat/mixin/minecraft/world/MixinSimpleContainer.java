package top.hendrixshen.magiclib.compat.mixin.minecraft.world;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import top.hendrixshen.magiclib.compat.annotation.Remap;

@Mixin(SimpleContainer.class)
public abstract class MixinSimpleContainer {
    @Shadow
    public abstract ItemStack addItem(ItemStack itemStack);

    @Remap("method_7659")
    public void fromTag(ListTag listTag) {
        for (int i = 0; i < listTag.size(); ++i) {
            ItemStack itemStack = ItemStack.of(listTag.getCompound(i));
            if (!itemStack.isEmpty()) {
                this.addItem(itemStack);
            }
        }
    }
}
