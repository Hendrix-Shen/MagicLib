package top.hendrixshen.magiclib.mixin.compat.minecraft.world;

import net.minecraft.world.SimpleContainer;
import org.spongepowered.asm.mixin.Mixin;

import top.hendrixshen.magiclib.api.compat.minecraft.world.SimpleContainerCompat;
import top.hendrixshen.magiclib.compat.minecraft.api.world.SimpleContainerCompatApi;

//#if MC >= 12106
//$$ import net.minecraft.world.item.ItemStack;
//$$ import net.minecraft.world.level.storage.ValueInput;
//#else
import net.minecraft.nbt.ListTag;
//#endif

//#if MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif

//#if MC < 11600
//$$ import net.minecraft.world.item.ItemStack;
//#endif

@Mixin(SimpleContainer.class)
public abstract class MixinSimpleContainer implements SimpleContainerCompatApi {
    @Override
    public void fromTagCompat(
            //#if MC >= 12106
            //$$ ValueInput.TypedInputList<ItemStack> typedInputList
            //#else
            ListTag listTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
            //#endif
    ) {
        SimpleContainerCompat simpleContainerCompat = SimpleContainerCompat.of((SimpleContainer) (Object) this);
        simpleContainerCompat.fromTag(
                //#if MC >= 12106
                //$$ typedInputList
                //#else
                listTag
                //#if MC > 12004
                //$$ , provider
                //#endif
                //#endif
        );
    }
}
