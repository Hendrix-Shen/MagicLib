package top.hendrixshen.magiclib.compat.minecraft.api.world.level;

import net.minecraft.resources.ResourceLocation;
import top.hendrixshen.magiclib.compat.api.UnImplCompatApiException;

public interface LevelCompatApi {
    default ResourceLocation getDimensionLocation() {
        throw new UnImplCompatApiException();
    }

    default int getMinBuildHeightCompat() {
        throw new UnImplCompatApiException();
    }

    //#if MC <= 11650
    default int getMinBuildHeight() {
        return this.getMinBuildHeightCompat();
    }
    //#endif
}
