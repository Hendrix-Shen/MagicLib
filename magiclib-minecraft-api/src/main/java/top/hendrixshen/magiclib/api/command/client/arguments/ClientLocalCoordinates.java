package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
import net.minecraft.util.Mth;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Objects;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.coordinates.LocalCoordinates}.
 *
 * <p>The parsing logic is identical to the vanilla implementation, since it does not depend on the
 * command source. Only the resolution methods use the {@link ClientCommandSource} instead of a
 * {@code CommandSourceStack}.</p>
 */
public class ClientLocalCoordinates implements ClientCoordinates {
    private final double left;
    private final double up;
    private final double forwards;

    /**
     * Creates local coordinates from the given offsets.
     *
     * @param left     the left offset
     * @param up       the up offset
     * @param forwards the forwards offset
     */
    public ClientLocalCoordinates(double left, double up, double forwards) {
        this.left = left;
        this.up = up;
        this.forwards = forwards;
    }

    @Override
    public Vec3 getPosition(ClientCommandSource source) {
        Vec2 rotation = source.getRotation();
        Vec3 anchor = source.getPosition();
        // The following mirrors the pre-1.21 vanilla local-coordinate resolution: the local
        // offsets are rotated by the entity's rotation to produce the world offset. Newer MC
        // versions factor this into Vec3#applyLocalCoordinatesToRotation.
        float cosYaw = Mth.cos((rotation.y + 90.0F) * (float) (Math.PI / 180.0));
        float sinYaw = Mth.sin((rotation.y + 90.0F) * (float) (Math.PI / 180.0));
        float cosPitch = Mth.cos(-rotation.x * (float) (Math.PI / 180.0));
        float sinPitch = Mth.sin(-rotation.x * (float) (Math.PI / 180.0));
        float cosPitch90 = Mth.cos((-rotation.x + 90.0F) * (float) (Math.PI / 180.0));
        float sinPitch90 = Mth.sin((-rotation.x + 90.0F) * (float) (Math.PI / 180.0));
        Vec3 forwardsVec = new Vec3(cosYaw * cosPitch, sinPitch, sinYaw * cosPitch);
        Vec3 upVec = new Vec3(cosYaw * cosPitch90, sinPitch90, sinYaw * cosPitch90);
        Vec3 leftVec = forwardsVec.cross(upVec).scale(-1.0);
        double x = forwardsVec.x * this.forwards + upVec.x * this.up + leftVec.x * this.left;
        double y = forwardsVec.y * this.forwards + upVec.y * this.up + leftVec.y * this.left;
        double z = forwardsVec.z * this.forwards + upVec.z * this.up + leftVec.z * this.left;
        return new Vec3(anchor.x + x, anchor.y + y, anchor.z + z);
    }

    @Override
    public Vec2 getRotation(ClientCommandSource source) {
        return Vec2.ZERO;
    }

    @Override
    public boolean isXRelative() {
        return true;
    }

    @Override
    public boolean isYRelative() {
        return true;
    }

    @Override
    public boolean isZRelative() {
        return true;
    }

    /**
     * Parses local coordinates from the reader.
     *
     * @param reader the reader
     * @return the parsed coordinates
     * @throws CommandSyntaxException if the input is malformed
     */
    public static ClientLocalCoordinates parse(StringReader reader) throws CommandSyntaxException {
        int start = reader.getCursor();
        double left = readCoordinate(reader, start);

        if (!reader.canRead() || reader.peek() != ' ') {
            reader.setCursor(start);
            throw ClientVec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
        }

        reader.skip();
        double up = readCoordinate(reader, start);

        if (!reader.canRead() || reader.peek() != ' ') {
            reader.setCursor(start);
            throw ClientVec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
        }

        reader.skip();
        double forwards = readCoordinate(reader, start);
        return new ClientLocalCoordinates(left, up, forwards);
    }

    private static double readCoordinate(StringReader reader, int start) throws CommandSyntaxException {
        if (!reader.canRead()) {
            throw WorldCoordinate.ERROR_EXPECTED_DOUBLE.createWithContext(reader);
        }

        if (reader.peek() != '^') {
            reader.setCursor(start);
            throw ClientVec3Argument.ERROR_MIXED_TYPE.createWithContext(reader);
        }

        reader.skip();
        return reader.canRead() && reader.peek() != ' ' ? reader.readDouble() : 0.0;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ClientLocalCoordinates)) {
            return false;
        }

        ClientLocalCoordinates that = (ClientLocalCoordinates) object;
        return this.left == that.left && this.up == that.up && this.forwards == that.forwards;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.left, this.up, this.forwards);
    }
}
