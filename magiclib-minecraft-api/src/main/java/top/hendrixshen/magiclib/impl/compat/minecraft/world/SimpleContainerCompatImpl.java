package top.hendrixshen.magiclib.impl.compat.minecraft.world;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.SimpleContainerCompat;

//#if MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif

//#if MC < 11600
//$$ import net.minecraft.world.item.ItemStack;
//#endif

public class SimpleContainerCompatImpl extends AbstractCompat<SimpleContainer> implements SimpleContainerCompat {
    public SimpleContainerCompatImpl(@NotNull SimpleContainer type) {
        super(type);
    }

    @Override
    public void fromTag(
            ListTag listTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
    ) {
        //#if MC > 11502
        this.get().fromTag(
                listTag
                //#if MC > 12004
                //$$ , provider
                //#endif
        );
        //#else
        //$$ for (int i = 0; i < listTag.size(); i++) {
        //$$     ItemStack itemStack = ItemStack.of(listTag.getCompound(i));
        //$$
        //$$     if (!itemStack.isEmpty()) {
        //$$         this.get().addItem(itemStack);
        //$$     }
        //$$ }
        //#endif
    }
}
