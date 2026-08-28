package top.hendrixshen.magiclib.api.command.client.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.arguments.coordinates.WorldCoordinate;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

import top.hendrixshen.magiclib.api.command.client.ClientCommandSource;

import java.util.Objects;

/**
 * The client-side counterpart of {@link net.minecraft.commands.arguments.coordinates.WorldCoordinates}.
 *
 * <p>The parsing logic is identical to the vanilla implementation, since it does not depend on the
 * command source. Only the resolution methods use the {@link ClientCommandSource} instead of a
 * {@code CommandSourceStack}.</p>
 */
public class ClientWorldCoordinates implements ClientCoordinates {
    private final WorldCoordinate x;
    private final WorldCoordinate y;
    private final WorldCoordinate z;

    /**
     * Creates world coordinates from the given axes.
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    public ClientWorldCoordinates(WorldCoordinate x, WorldCoordinate y, WorldCoordinate z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public Vec3 getPosition(ClientCommandSource source) {
        Vec3 vec3 = source.getPosition();
        return new Vec3(this.x.get(vec3.x), this.y.get(vec3.y), this.z.get(vec3.z));
    }

    @Override
    public Vec2 getRotation(ClientCommandSource source) {
        Vec2 vec2 = source.getRotation();
        return new Vec2((float) this.x.get(vec2.x), (float) this.y.get(vec2.y));
    }

    @Override
    public boolean isXRelative() {
        return this.x.isRelative();
    }

    @Override
    public boolean isYRelative() {
        return this.y.isRelative();
    }

    @Override
    public boolean isZRelative() {
        return this.z.isRelative();
    }

    /**
     * Parses world coordinates from the reader.
     *
     * @param reader the reader
     * @return the parsed coordinates
     * @throws CommandSyntaxException if the input is malformed
     */
    public static ClientWorldCoordinates parse(StringReader reader) throws CommandSyntaxException {
        int cursor = reader.getCursor();
        WorldCoordinate x = WorldCoordinate.parseInt(reader);

        if (reader.canRead() && reader.peek() == ' ') {
            reader.skip();
            WorldCoordinate y = WorldCoordinate.parseInt(reader);

            if (reader.canRead() && reader.peek() == ' ') {
                reader.skip();
                WorldCoordinate z = WorldCoordinate.parseInt(reader);
                return new ClientWorldCoordinates(x, y, z);
            }
        }

        reader.setCursor(cursor);
        throw ClientVec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
    }

    /**
     * Parses world coordinates from the reader, optionally centering integer coordinates.
     *
     * @param reader         the reader
     * @param centerIntegers whether integer coordinates should be centered
     * @return the parsed coordinates
     * @throws CommandSyntaxException if the input is malformed
     */
    public static ClientWorldCoordinates parse(StringReader reader, boolean centerIntegers) throws CommandSyntaxException {
        int cursor = reader.getCursor();
        WorldCoordinate x = WorldCoordinate.parseDouble(reader, centerIntegers);

        if (reader.canRead() && reader.peek() == ' ') {
            reader.skip();
            WorldCoordinate y = WorldCoordinate.parseDouble(reader, false);

            if (reader.canRead() && reader.peek() == ' ') {
                reader.skip();
                WorldCoordinate z = WorldCoordinate.parseDouble(reader, centerIntegers);
                return new ClientWorldCoordinates(x, y, z);
            }
        }

        reader.setCursor(cursor);
        throw ClientVec3Argument.ERROR_NOT_COMPLETE.createWithContext(reader);
    }

    /**
     * Creates coordinates relative to the anchor.
     *
     * @return the coordinates
     */
    public static ClientWorldCoordinates current() {
        return new ClientWorldCoordinates(
                new WorldCoordinate(true, 0.0),
                new WorldCoordinate(true, 0.0),
                new WorldCoordinate(true, 0.0)
        );
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (!(object instanceof ClientWorldCoordinates)) {
            return false;
        }

        ClientWorldCoordinates that = (ClientWorldCoordinates) object;
        return this.x.equals(that.x) && this.y.equals(that.y) && this.z.equals(that.z);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.x, this.y, this.z);
    }
}
