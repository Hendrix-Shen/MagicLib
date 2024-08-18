package top.hendrixshen.magiclib.api.compat.minecraft.world.level;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.world.level.state.BlockStateCompat;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.level.LevelCompatImpl;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface LevelCompat extends Provider<Level> {
    static @NotNull LevelCompat of(Level level) {
        return new LevelCompatImpl(level);
    }

    BlockState getBlockState(BlockPos blockPos);

    BlockStateCompat getBlockStateCompat(BlockPos blockPos);

    ResourceLocation getDimensionLocation();

    int getMinBuildHeight();
}
