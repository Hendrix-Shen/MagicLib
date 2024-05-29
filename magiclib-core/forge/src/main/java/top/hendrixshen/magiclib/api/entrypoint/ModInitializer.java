package top.hendrixshen.magiclib.api.entrypoint;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.platform.DistType;

/**
 * Fabric style ModInitializer
 * <p>
 * Provide a Fabric-style ModInitializer for cross-platform mod.
 *
 * <p>
 * If you want to use the custom dependency checker provided by MagicLib on Forge,
 * you must implement this interface.
 */
public interface ModInitializer {
    /**
     * Call this method in your constructor.
     */
    default void construct() {
        if (MagicLib.getInstance().getCurrentPlatform().matchesDist(DistType.CLIENT)) {
            this.onInitializeClient();
        } else if (MagicLib.getInstance().getCurrentPlatform().matchesDist(DistType.SERVER)) {
            this.onInitializeServer();
        }

        this.onInitialize();
    }

    /**
     * Runs the mod initializer on the client environment.
     */
    void onInitializeClient();

    /**
     * Runs the mod initializer on the server environment.
     */
    void onInitializeServer();

    /**
     * Runs the mod initializer.
     */
    void onInitialize();
}
