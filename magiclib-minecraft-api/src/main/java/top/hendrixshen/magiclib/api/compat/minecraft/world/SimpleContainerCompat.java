package top.hendrixshen.magiclib.api.compat.minecraft.world;

import org.jetbrains.annotations.NotNull;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;

// CHECKSTYLE.OFF: ImportOrder
//#if MC > 12004
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
            ListTag listTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
            // CHECKSTYLE.ON: SeparatorWrap
            // CHECKSTYLE.ON: NoWhitespaceBefore
    );
}
