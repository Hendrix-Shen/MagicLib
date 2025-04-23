package top.hendrixshen.magiclib.api.malilib.config.migration;

import top.hendrixshen.magiclib.api.malilib.config.MagicConfigHandler;

public interface ConfigMigrator {
    /**
     * Migrates the configuration settings using the provided config handler.
     *
     * @param configHandler The handler for the configuration settings
     * @return {@code true} if migration has data changes, {@code false} otherwise
     */
    boolean migrate(MagicConfigHandler configHandler);

    /**
     * Determines whether the configuration settings should be migrated.
     *
     * @param configHandler The handler for the configuration settings
     * @return {@code true} if the configuration settings should be migrated, {@code false} otherwise
     */
    boolean shouldMigrate(MagicConfigHandler configHandler);
}
