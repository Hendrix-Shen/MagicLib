package top.hendrixshen.magiclib.impl.malilib.config.migration;

import com.google.common.collect.Maps;
import com.google.gson.JsonObject;
import fi.dy.masa.malilib.util.JsonUtils;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigHandler;
import top.hendrixshen.magiclib.api.malilib.config.migration.ConfigMigrator;
import top.hendrixshen.magiclib.util.collect.SimplePredicate;

import java.util.Map;

/**
 * A Simple config statistic renaming migrator.
 */
public class RenameStatisticMigrator implements ConfigMigrator {
    private final Map<String, String> migrateMapping = Maps.newLinkedHashMap();
    private final SimplePredicate<MagicConfigHandler> migratePredicate;

    public RenameStatisticMigrator(Map<String, String> migrateMapping, SimplePredicate<MagicConfigHandler> migratePredicate) {
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
            String oldName = entry.getKey();
            String newName = entry.getValue();

            JsonObject internalObj = JsonUtils.getNestedObject(loadedJson, "internal", false);

            if (internalObj == null) {
                MagicLib.getLogger().warn("[RenameStatisticMigrator-{}]Skipped statistic renaming because missing internal data.",
                        configHandler.getIdentifier());
                return false;
            }

            JsonObject statisticObj = JsonUtils.getNestedObject(internalObj, "configStatistic", false);

            if (statisticObj == null) {
                MagicLib.getLogger().warn("[RenameStatisticMigrator-{}]Skipped statistic renaming because missing configStatistic data.",
                        configHandler.getIdentifier());
                return false;
            }

            if (!statisticObj.has(oldName)) {
                MagicLib.getLogger().warn("[RenameStatisticMigrator-{}]Skipped statistic renaming because source config does not exist(source={}, destination={}).",
                        configHandler.getIdentifier(), oldName, newName);
            }

            if (statisticObj.has(newName)) {
                MagicLib.getLogger().warn("[RenameStatisticMigrator-{}]Skipped statistic renaming because destination config already exists(source={}, destination={}).",
                        configHandler.getIdentifier(), oldName, newName);
            }

            statisticObj.add(newName, statisticObj.get(oldName));
            ret = true;
            MagicLib.getLogger().info("[RenameStatisticMigrator-{}]Renamed statistic: {} -> {}",
                    configHandler.getIdentifier(), oldName, newName);
        }

        return ret;
    }

    @Override
    public boolean shouldMigrate(MagicConfigHandler configHandler) {
        return this.migratePredicate.test(configHandler);
    }
}
