package top.hendrixshen.magiclib.api.compat.minecraft.world;

import org.jetbrains.annotations.NotNull;

import net.minecraft.world.SimpleContainer;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 12106
//$$ import net.minecraft.world.item.ItemStack;
//$$ import net.minecraft.world.level.storage.ValueInput;
//#else
import net.minecraft.nbt.ListTag;
//#endif

//#if 12106 > MC &&  MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif
// CHECKSTYLE.ON: ImportOrder

import top.hendrixshen.magiclib.impl.compat.minecraft.world.SimpleContainerCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface SimpleContainerCompat extends Provider<SimpleContainer> {
    static @NotNull SimpleContainerCompat of(SimpleContainer simpleContainer) {
        return new SimpleContainerCompatImpl(simpleContainer);
    }

    void fromTag(
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
    );
}
