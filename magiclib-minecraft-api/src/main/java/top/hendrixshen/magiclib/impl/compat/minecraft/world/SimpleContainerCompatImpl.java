package top.hendrixshen.magiclib.impl.compat.minecraft.world;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.SimpleContainer;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12106
//$$ import net.minecraft.world.level.storage.ValueInput;
//#else
import net.minecraft.nbt.ListTag;
//#endif

//#if 12106 <= MC || MC < 11600
//$$ import net.minecraft.world.item.ItemStack;
//#endif

//#if 12106 > MC &&  MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.api.compat.AbstractCompat;
import top.hendrixshen.magiclib.api.compat.minecraft.world.SimpleContainerCompat;

public class SimpleContainerCompatImpl extends AbstractCompat<SimpleContainer> implements SimpleContainerCompat {
    public SimpleContainerCompatImpl(@NotNull SimpleContainer type) {
        super(type);
    }

    @Override
    public void fromTag(
            // CHECKSTYLE.OFF: NoWhitespaceBefore
            // CHECKSTYLE.OFF: SeparatorWrap
            //#if MC >= 12106
            //$$ ValueInput.TypedInputList<ItemStack> typedInputList
            //#else
            ListTag listTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    ) {
        //#if MC > 11502
        this.get().fromTag(
                // CHECKSTYLE.OFF: NoWhitespaceBefore
                // CHECKSTYLE.OFF: SeparatorWrap
                //#if MC >= 12106
                //$$ typedInputList
                //#else
                listTag
                //#if MC > 12004
                //$$ , provider
                //#endif
                //#endif
                // CHECKSTYLE.ON: SeparatorWrap
                // CHECKSTYLE.ON: NoWhitespaceBefore
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
