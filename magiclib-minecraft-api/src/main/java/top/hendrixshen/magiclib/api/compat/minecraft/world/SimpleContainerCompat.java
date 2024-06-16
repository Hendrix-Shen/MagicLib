package top.hendrixshen.magiclib.api.compat.minecraft.world;

import net.minecraft.nbt.ListTag;
import net.minecraft.world.SimpleContainer;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.SimpleContainerCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

//#if MC > 12004
//$$ import net.minecraft.core.HolderLookup;
//#endif

public interface SimpleContainerCompat extends Provider<SimpleContainer> {
    static @NotNull SimpleContainerCompat of(SimpleContainer simpleContainer) {
        return new SimpleContainerCompatImpl(simpleContainer);
    }

    void fromTag(
            ListTag listTag
            //#if MC > 12004
            //$$ , HolderLookup.Provider provider
            //#endif
    );
}
