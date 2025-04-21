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
 * A Simple config category renaming migrator.
 *
 * <p>
 * Typically used for migrations where the name of an entire configuration category has changed.
 * </p>
 */
public class RenameCategoryMigrator implements ConfigMigrator {
    private final Map<String, String> renameMapping = Maps.newHashMap();
    private final SimplePredicate<MagicConfigHandler> migratePredicate;

    public RenameCategoryMigrator(Map<String, String> renameMapping, SimplePredicate<MagicConfigHandler> migratePredicate) {
        this.renameMapping.putAll(renameMapping);
        this.migratePredicate = migratePredicate;
    }

    public void addRenameMapping(String oldName, String newName) {
        this.renameMapping.put(oldName, newName);
    }

    public void addRenameMapping(Map<String, String> renameMapping) {
        this.renameMapping.putAll(renameMapping);
    }

    @Override
    public boolean migrate(MagicConfigHandler configHandler) {
        JsonObject loadedJson = configHandler.getLoadedJson();
        boolean ret = false;

        for (Map.Entry<String, String> entry : this.renameMapping.entrySet()) {
            String oldCategory = entry.getKey();
            String newCategory = entry.getValue();
            JsonObject oldCategoryObj = JsonUtils.getNestedObject(loadedJson, oldCategory, false);
            JsonObject newCategoryObj = JsonUtils.getNestedObject(loadedJson, newCategory, false);

            if (oldCategoryObj == null) {
                MagicLib.getLogger().warn("[RenameCategoryMigrator-{}]Skipped category renaming because source category {} does not exist.",
                        configHandler.getIdentifier(), oldCategory);
                continue;
            }

            if (newCategoryObj != null) {
                MagicLib.getLogger().warn("[RenameCategoryMigrator-{}]Skipped category renaming because destination category {} already exists.",
                        configHandler.getIdentifier(), newCategory);
                continue;
            }

            loadedJson.add(newCategory, oldCategoryObj);
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
