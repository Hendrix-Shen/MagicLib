package top.hendrixshen.magiclib.api.command.client.arguments;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;
import top.hendrixshen.magiclib.api.compat.minecraft.core.BlockPosCompat;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.coordinates.Coordinates}.
 *
 * <p>Unlike the vanilla interface, which requires a {@code CommandSourceStack} to resolve the
 * coordinates, this interface resolves them against a {@link ClientCommandSource}, so that it can
 * be used inside client-side commands without touching the server.</p>
 */
public interface ClientCoordinates {
    /**
     * Resolves the coordinates to an absolute position.
     *
     * @param source the client command source used as the anchor
     * @return the resolved position
     */
    Vec3 getPosition(ClientCommandSource source);

    /**
     * Resolves the coordinates to a rotation.
     *
     * @param source the client command source used as the anchor
     * @return the resolved rotation
     */
    Vec2 getRotation(ClientCommandSource source);

    /**
     * Resolves the coordinates to a block position.
     *
     * @param source the client command source used as the anchor
     * @return the resolved block position
     */
    default BlockPos getBlockPos(ClientCommandSource source) {
        return BlockPosCompat.containing(this.getPosition(source));
    }

    /**
     * Whether the x coordinate is relative to the anchor.
     *
     * @return true if relative
     */
    boolean isXRelative();

    /**
     * Whether the y coordinate is relative to the anchor.
     *
     * @return true if relative
     */
    boolean isYRelative();

    /**
     * Whether the z coordinate is relative to the anchor.
     *
     * @return true if relative
     */
    boolean isZRelative();
}
