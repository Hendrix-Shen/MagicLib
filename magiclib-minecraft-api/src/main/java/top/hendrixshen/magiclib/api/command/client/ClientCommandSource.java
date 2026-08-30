package top.hendrixshen.magiclib.api.command.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;

// CHECKSTYLE.OFF: ImportOrder
//#if MC >= 1.15
import net.minecraft.client.multiplayer.ClientLevel;
//#else
//$$ import net.minecraft.client.multiplayer.MultiPlayerLevel;
//#endif
// CHECKSTYLE.ON: ImportOrder

/**
 * The source of a client-side command.
 *
 * <p>Client-side commands are executed entirely on the client thread, so they
 * work in both singleplayer and multiplayer without being sent to the server.</p>
 */
public interface ClientCommandSource {
    /**
     * Sends a success message to the client chat.
     *
     * @param message the success message
     */
    void sendSuccess(Component message);

    /**
     * Sends a failure message to the client chat.
     *
     * @param message the failure message
     */
    void sendFailure(Component message);

    /**
     * Gets the client instance used to run the command.
     *
     * @return the client
     */
    Minecraft getClient();

    /**
     * Gets the player that used the command.
     *
     * @return the player
     */
    LocalPlayer getPlayer();

    /**
     * Gets the level where the player used the command.
     *
     * @return the level
     */
    //#if MC >= 1.15
    ClientLevel getLevel();
    //#else
    //$$ MultiPlayerLevel getLevel();
    //#endif

    /**
     * Gets the position of the player that used the command.
     *
     * @return the position
     */
    default Vec3 getPosition() {
        return this.getPlayer() != null ? this.getPlayer().position() : Vec3.ZERO;
    }

    /**
     * Gets the rotation of the player that used the command.
     *
     * @return the rotation
     */
    default Vec2 getRotation() {
        return this.getPlayer() != null ? this.getPlayer().getRotationVector() : Vec2.ZERO;
    }

    /**
     * Gets the player entity that used the command.
     *
     * @return the player entity, or null if the player is not available
     */
    default Entity getEntity() {
        return this.getPlayer();
    }
}
