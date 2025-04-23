package top.hendrixshen.magiclib.impl.malilib.config.migration;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.util.JsonUtils;
import lombok.Data;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigHandler;
import top.hendrixshen.magiclib.api.malilib.config.migration.ConfigMigrator;
import top.hendrixshen.magiclib.util.collect.SimplePredicate;

import java.util.List;

/**
 * A config relocation migrator.
 *
 * <p>
 * Basically an integrated version of {@link RenameCategoryMigrator} and {@link RenameConfigMigrator}.
 * If not a complex migration, it is recommended to use the non-integrated version,
 * as it is slower to compare one by one
 * </p>
 */
public class ConfigRelocationMigrator implements ConfigMigrator {
    private final List<MigrationMapping> migrateMapping = Lists.newArrayList();
    private final SimplePredicate<MagicConfigHandler> migratePredicate;

    public ConfigRelocationMigrator(List<MigrationMapping> migrateMapping, SimplePredicate<MagicConfigHandler> migratePredicate) {
        this.migrateMapping.addAll(migrateMapping);
        this.migratePredicate = migratePredicate;
    }

    public void addMigrateMapping(String oldName, String oldCategory, String newName, String newCategory) {
        this.migrateMapping.add(new MigrationMapping(oldName, oldCategory, newName, newCategory));
    }

    public void addMigrateMapping(List<MigrationMapping> migrateMapping) {
        this.migrateMapping.addAll(migrateMapping);
    }

    @Override
    public boolean migrate(MagicConfigHandler configHandler) {
        JsonObject loadedJson = configHandler.getLoadedJson();
        boolean isMigrated = false;

        for (MigrationMapping mapping : this.migrateMapping) {
            JsonObject oldCategoryObj = JsonUtils.getNestedObject(loadedJson, mapping.getOldCategory(), false);

            if (oldCategoryObj == null) {
                MagicLib.getLogger().warn("[ConfigRelocationMigrator-{}]Skipped migrate config, because source category does not exist(source={}.{}, destination={}.{}).",
                        configHandler.getIdentifier(), mapping.getOldCategory(), mapping.getOldName(), mapping.getNewCategory(), mapping.getNewName());
                continue;
            }

            JsonElement oldConfigObj = oldCategoryObj.get(mapping.getOldName());

            if (oldConfigObj == null) {
                MagicLib.getLogger().warn("[ConfigRelocationMigrator-{}]Skipped migrate config, because source config does not exist(source={}.{}, destination={}.{}).",
                        configHandler.getIdentifier(),
                        mapping.getOldCategory(), mapping.getOldName(),
                        mapping.getNewCategory(), mapping.getNewName());
                continue;
            }

            JsonObject newCategoryObj = JsonUtils.getNestedObject(loadedJson, mapping.getNewCategory(), true);
            assert newCategoryObj != null;

            if (newCategoryObj.has(mapping.getNewName())) {
                MagicLib.getLogger().warn("[ConfigRelocationMigrator-{}]Skipped migrate config, because destination config already exists(source={}.{}, destination={}.{}).",
                        configHandler.getIdentifier(),
                        mapping.getOldCategory(), mapping.getOldName(),
                        mapping.getNewCategory(), mapping.getNewName());
                continue;
            }

            newCategoryObj.add(mapping.getNewName(), oldConfigObj);
            isMigrated = true;
            MagicLib.getLogger().info("[ConfigRelocationMigrator-{}]Migrated config {}.{} -> {}.{}",
                    configHandler.getIdentifier(),
                    mapping.getOldCategory(), mapping.getOldName(),
                    mapping.getNewCategory(), mapping.getNewName());
        }

        return isMigrated;
    }

    @Override
    public boolean shouldMigrate(MagicConfigHandler configHandler) {
        return this.migratePredicate.test(configHandler);
    }

    @Data
    public static class MigrationMapping {
        private final String oldCategory;
        private final String oldName;
        private final String newCategory;
        private final String newName;
    }
}
