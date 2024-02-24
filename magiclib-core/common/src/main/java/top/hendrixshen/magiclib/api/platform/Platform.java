package top.hendrixshen.magiclib.api.platform;

import top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter;
import top.hendrixshen.magiclib.util.collect.ValueContainer;

import java.nio.file.Path;
import java.util.Collection;

public interface Platform {
    /**
     * Get the current game working directory.
     *
     * @return The working directory.
     */
    Path getGameFolder();

    /**
     * Get the current directory for game configuration files.
     *
     * @return The configuration directory.
     */
    Path getConfigFolder();

    /**
     * Get the current directory for mod directory.
     *
     * @return The mod directory.
     */
    Path getModsFolder();

    /**
     * Get the current running platform.
     *
     * @return The platform.
     */
    PlatformType getPlatformType();

    /**
     * Get the current running platform name.
     *
     * @return The platform name.
     */
    default String getPlatformName() {
        return this.getPlatformType().getName();
    }

    /**
     * Get the current running environment.
     *
     * @return The environment.
     */
    DistType getCurrentDistType();

    /**
     * Match the current dist environment.
     *
     * @return True if dist matches.
     */
    boolean matchesDist(DistType distType);

    /**
     * Check if the Mod is loaded.
     * <p>
     * In forge-like, this is only true if the module is actually loaded.
     *
     * @return True if mod loaded.
     */
    boolean isModLoaded(String modIdentifier);

    /**
     * Check if the Mod is existed.
     * <p>
     * In fabric-like, the behaviour is consistent with
     * {@link Platform#isModLoaded(String) Platform#isModLoaded(String)}.
     *
     * <p>
     * In forge-like, the mod holds when it is discovered.
     *
     * @return True if mod loaded.
     */
    boolean isModExist(String modIdentifier);

    /**
     * Check if running in the development environment.
     *
     * @return True if running in the development environment.
     */
    boolean isDevelopmentEnvironment();

    /**
     * Get the Mod name.
     * See {@link top.hendrixshen.magiclib.api.platform.adapter.ModMetaDataAdapter ModMetaDataAdapter}.
     *
     * @return The Mod name.
     */
    default String getModName(String modIdentifier) {
        return this.getMod(modIdentifier).map(mod -> mod.getModMetaData().getName()).orElse("?");
    }

    /**
     * Get the Mod version.
     * See {@link top.hendrixshen.magiclib.api.platform.adapter.ModMetaDataAdapter ModMetaDataAdapter}.
     *
     * @return The Mod version.
     */
    default String getModVersion(String modIdentifier) {
        return this.getMod(modIdentifier).map(mod -> mod.getModMetaData().getVersion()).orElse("?");
    }

    /**
     * Get the Mod container.
     * See {@link top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter ModContainerAdapter}.
     *
     * @return The Mod container.
     */
    ValueContainer<ModContainerAdapter> getMod(String modIdentifier);

    /**
     * Gets all mod containers.
     * See {@link top.hendrixshen.magiclib.api.platform.adapter.ModContainerAdapter ModContainerAdapter}.
     *
     * @return A collection of all <b>loaded</b> mod containers.
     */
    Collection<ModContainerAdapter> getMods();

    /**
     * Get all mod identifiers.
     *
     * @return A collection of all <b>loaded</b> mod identifiers.
     */
    Collection<String> getModIds();
}

