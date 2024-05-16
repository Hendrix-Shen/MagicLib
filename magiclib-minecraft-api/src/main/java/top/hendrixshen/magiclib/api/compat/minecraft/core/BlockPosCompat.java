package top.hendrixshen.magiclib.api.compat.minecraft.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Position;
import net.minecraft.core.Vec3i;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.compat.minecraft.world.entity.EntityCompat;
import top.hendrixshen.magiclib.util.collect.Provider;

public interface BlockPosCompat extends Provider<BlockPos> {
    static @NotNull BlockPos blockPos(int x, int y, int z) {
        return new BlockPos(x, y, z);
    }

    static @NotNull BlockPos blockPos(double x, double y, double z) {
        return BlockPosCompat.containing(x, y, z);
    }

    static @NotNull BlockPos blockPos(@NotNull EntityCompat entity) {
        return entity.getBlockPosition();
    }

    static @NotNull BlockPos blockPos(@NotNull Vec3 vec3) {
        return BlockPosCompat.containing(vec3.x(), vec3.y(), vec3.z());
    }

    static @NotNull BlockPos blockPos(@NotNull Position position) {
        return BlockPosCompat.containing(position);
    }

    static @NotNull BlockPos blockPos(@NotNull Vec3i Vec3i) {
        return new BlockPos(Vec3i.getX(), Vec3i.getY(), Vec3i.getZ());
    }

    static @NotNull BlockPos containing(double x, double y, double z) {
        return new BlockPos(Mth.floor(x), Mth.floor(y), Mth.floor(z));
    }

    static @NotNull BlockPos containing(@NotNull Position arg) {
        return BlockPosCompat.containing(arg.x(), arg.y(), arg.z());
    }
}
