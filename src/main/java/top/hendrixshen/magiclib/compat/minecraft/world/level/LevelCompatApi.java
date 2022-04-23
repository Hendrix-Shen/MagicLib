package top.hendrixshen.magiclib.compat.minecraft.world.level;

import net.minecraft.resources.ResourceLocation;

public interface LevelCompatApi {
    default ResourceLocation getDimensionLocation() {
        throw new UnsupportedOperationException();
    }

    //#if MC <= 11650
    //$$ default int getMinBuildHeight() {
    //$$     return 0;
    //$$ }
    //#endif
}
