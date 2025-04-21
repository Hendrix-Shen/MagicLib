package top.hendrixshen.magiclib.impl.malilib.config.migration;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigHandler;
import top.hendrixshen.magiclib.api.malilib.config.migration.ConfigMigrator;
import top.hendrixshen.magiclib.util.collect.SimplePredicate;

import java.util.Map;

/**
 * A Simple config category renaming migrator.
 *
 * <p>
 * Typically used for migrations where the name of an entire configuration category has changed.
 * </p>
 */
public class RenameCategoryMigrator implements ConfigMigrator {
    private final Map<String, String> migrateMapping = Maps.newLinkedHashMap();
    private final SimplePredicate<MagicConfigHandler> migratePredicate;

    public RenameCategoryMigrator(Map<String, String> migrateMapping, SimplePredicate<MagicConfigHandler> migratePredicate) {
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

        for (Map.Entry<String, String> entry : this.migrateMapping.entrySet()) {
            String oldCategory = entry.getKey();
            String newCategory = entry.getValue();

            if (!loadedJson.has(oldCategory)) {
                MagicLib.getLogger().warn("[RenameCategoryMigrator-{}]Skipped category renaming because source category {} does not exist.",
                        configHandler.getIdentifier(), oldCategory);
                continue;
            }

            if (loadedJson.has(newCategory)) {
                MagicLib.getLogger().warn("[RenameCategoryMigrator-{}]Skipped category renaming because destination category {} already exists.",
                        configHandler.getIdentifier(), newCategory);
            }

            loadedJson.add(newCategory, loadedJson.get(oldCategory));
            ret = true;
            MagicLib.getLogger().info("[RenameCategoryMigrator-{}]Renamed category: {} -> {}",
                    configHandler.getIdentifier(), oldCategory, newCategory);
        }

        return ret;
    }

    @Override
    public boolean shouldMigrate(MagicConfigHandler configHandler) {
        return this.migratePredicate.test(configHandler);
    }
}
