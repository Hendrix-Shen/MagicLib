package top.hendrixshen.magiclib.api.compat.minecraft.world.level;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.level.LevelCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface LevelCompat extends Provider<Level> {
    static @NotNull LevelCompat of(Level level) {
        return new LevelCompatImpl(level);
    }

    ResourceLocation getDimensionLocation();

    int getMinBuildHeight();
}
