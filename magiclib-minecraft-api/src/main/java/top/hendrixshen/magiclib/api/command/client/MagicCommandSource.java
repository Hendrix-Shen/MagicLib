package top.hendrixshen.magiclib.api.command.client;

import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;

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
public interface MagicCommandSource {
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
}
