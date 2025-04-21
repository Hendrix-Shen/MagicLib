package top.hendrixshen.magiclib.impl.malilib.config.migration;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.util.JsonUtils;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigHandler;
import top.hendrixshen.magiclib.api.malilib.config.migration.ConfigMigrator;
import top.hendrixshen.magiclib.util.collect.SimplePredicate;

import java.util.Map;
import java.util.Map.Entry;

/**
 * A simple config renaming migrator.
 *
 * <p>
 * Typically used for migrations where the config name has changed.
 * </p>
 */
public class RenameConfigMigrator implements ConfigMigrator {
    private final Map<String, String> migrateMapping = Maps.newLinkedHashMap();
    private final SimplePredicate<MagicConfigHandler> migratePredicate;

    public RenameConfigMigrator(Map<String, String> migrateMapping, SimplePredicate<MagicConfigHandler> migratePredicate) {
        this.migrateMapping.putAll(migrateMapping);
        this.migratePredicate = migratePredicate;
    }

    public void addMigrateMapping(String oldName, String newName) {
        this.migrateMapping.put(oldName, newName);
    }

    public void addMigrateMapping(Map<String, String> renameMapping) {
        this.migrateMapping.putAll(renameMapping);
    }

    @Override
    public boolean migrate(MagicConfigHandler configHandler) {
        JsonObject loadedJson = configHandler.getLoadedJson();
        boolean ret = false;

        for (String category : configHandler.getConfigManager().getCategories()) {
            JsonObject obj = JsonUtils.getNestedObject(loadedJson, category, false);

            if (obj == null) {
                continue;
            }

            for (Entry<String, String> entry : this.migrateMapping.entrySet()) {
                String oldName = entry.getKey();
                String newName = entry.getValue();

                if (!obj.has(oldName)) {
                    MagicLib.getLogger().warn("[RenameConfigMigrator-{}]Skipped renaming config, because source config does not exist(source={}.{}, destination={}.{}).",
                            configHandler.getIdentifier(), category, oldName, category, newName);
                    continue;
                }

                if (obj.has(newName)) {
                    MagicLib.getLogger().warn("[RenameConfigMigrator-{}]Skipped renaming config, because destination config already exists(source={}.{}, destination={}.{}).",
                            configHandler.getIdentifier(), category, oldName, category, newName);
                    continue;
                }

                obj.add(newName, obj.get(oldName));
                ret = true;
                MagicLib.getLogger().info("[RenameConfigMigrator-{}]Renamed config: {}.{} -> {}.{}",
                        configHandler.getIdentifier(), category, oldName, category, newName);
            }
        }

        return ret;
    }

    @Override
    public boolean shouldMigrate(MagicConfigHandler configHandler) {
        return this.migratePredicate.test(configHandler);
    }
}
