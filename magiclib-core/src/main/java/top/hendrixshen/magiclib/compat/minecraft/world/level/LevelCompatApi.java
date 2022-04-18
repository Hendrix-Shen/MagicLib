package top.hendrixshen.magiclib.compat.minecraft.world.level;

import net.minecraft.resources.ResourceLocation;

public interface LevelCompatApi {
    default ResourceLocation getDimensionLocation() {
        return null;
    }
}
