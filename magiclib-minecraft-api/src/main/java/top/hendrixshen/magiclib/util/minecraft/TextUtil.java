package top.hendrixshen.magiclib.util.minecraft;

import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.Vec3i;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.impl.compat.minecraft.world.level.dimension.DimensionWrapper;

/**
 * Reference to <a href="https://github.com/TISUnion/Carpet-TIS-Addition/">Carpet-TIS-Addition</a>
 * <p>
 * Minecraft related stuffs -> String
 */
public class TextUtil {
    public static String tp(@NotNull Vec3 pos) {
        return String.format("/tp %s %s %s", pos.x(), pos.y(), pos.z());
    }

    public static String tp(@NotNull Vec3i pos) {
        return String.format("/tp %d %d %d", pos.getX(), pos.getY(), pos.getZ());
    }

    public static String tp(@NotNull ChunkPos pos) {
        return String.format("/tp %d ~ %d", pos.x * 16 + 8, pos.z * 16 + 8);
    }

    public static @NotNull String tp(Vec3 pos, DimensionWrapper dimensionType) {
        return String.format("/execute in %s run", dimensionType) + TextUtil.tp(pos).replace('/', ' ');
    }

    public static @NotNull String tp(Vec3i pos, DimensionWrapper dimensionType) {
        return String.format("/execute in %s run", dimensionType) + TextUtil.tp(pos).replace('/', ' ');
    }

    public static @NotNull String tp(ChunkPos pos, DimensionWrapper dimensionType) {
        return String.format("/execute in %s run", dimensionType) + TextUtil.tp(pos).replace('/', ' ');
    }

    public static String tp(Entity entity) {
        if (entity instanceof Player) {
            String name = ((Player) entity).getGameProfile().getName();
            return String.format("/tp %s", name);
        }

        String uuid = entity.getUUID().toString();
        return String.format("/tp %s", uuid);
    }

    public static String coordinate(@NotNull Vec3 pos) {
        return String.format("[%.1f, %.1f, %.1f]", pos.x(), pos.y(), pos.z());
    }

    public static String coordinate(@NotNull Vec3i pos) {
        return String.format("[%d, %d, %d]", pos.getX(), pos.getY(), pos.getZ());
    }

    public static String coordinate(@NotNull ChunkPos pos) {
        return String.format("[%d, %d]", pos.x, pos.z);
    }

    public static String vector(@NotNull Vec3 vec, int digits) {
        return String.format("(%s, %s, %s)", StringUtil.fractionDigit(vec.x(), digits),
                StringUtil.fractionDigit(vec.y(), digits), StringUtil.fractionDigit(vec.z(), digits));
    }

    public static String vector(Vec3 vec) {
        return TextUtil.vector(vec, 2);
    }

    public static String block(Block block) {
        return ResourceLocationUtil.id(block).toString();
    }

    public static @NotNull String block(BlockState blockState) {
        return BlockStateParser.serialize(blockState);
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T>> String property(@NotNull Property<T> property, Object value) {
        return property.getName((T) value);
    }
}
