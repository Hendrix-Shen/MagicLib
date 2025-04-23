package top.hendrixshen.magiclib.impl.malilib.config.migration;

import com.google.gson.JsonObject;
import fi.dy.masa.malilib.util.JsonUtils;

import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigHandler;
import top.hendrixshen.magiclib.api.malilib.config.migration.ConfigMigrator;
import top.hendrixshen.magiclib.util.collect.SimplePredicate;

/**
 * A config version migrator.
 *
 * <p>
 * Typically used to finalise upgrade config file.
 * </p>
 */
public class VersionMigrator implements ConfigMigrator {
    private final int newVersion;
    private final SimplePredicate<MagicConfigHandler> migratePredicate;

    public VersionMigrator(int newVersion, SimplePredicate<MagicConfigHandler> migratePredicate) {
        this.newVersion = newVersion;
        this.migratePredicate = migratePredicate;
    }

    @Override
    public boolean migrate(MagicConfigHandler configHandler) {
        JsonObject loadedJson = configHandler.getLoadedJson();
        int oldVersion = VersionMigrator.tryGetConfigVersion(loadedJson);

        if (oldVersion == this.newVersion) {
            MagicLib.getLogger().info("[VersionMigrator-{}]Cannot migrant to same version {}",
                    configHandler.getIdentifier(), this.newVersion);
            return false;
        }

        JsonObject internal = JsonUtils.getNestedObject(loadedJson, "internal", true);
        assert internal != null;
        JsonObject global = JsonUtils.getNestedObject(internal, "global", true);
        assert global != null;
        global.addProperty("config_version", this.newVersion);
        internal.add("global", global);
        loadedJson.add("internal", internal);
        MagicLib.getLogger().info("[VersionMigrator-{}]Migrated config version from {} to {}",
                configHandler.getIdentifier(), oldVersion >= 0 ? oldVersion : "unknown", this.newVersion);
        return true;
    }

    @Override
    public boolean shouldMigrate(MagicConfigHandler configHandler) {
        return this.migratePredicate.test(configHandler);
    }

    /**
     * Attempts to retrieve the config version from a JSON object.
     *
     * <p>This method attempts to parse the following supported formats:
     * <ol>
     *   <li>MagicLib 0.8+: Looks for "internal" → "global" → "config_version" path</li>
     *   <li>MagicLib 0.3: Directly checks the "configVersion" field in root object</li>
     * </ol>
     *
     * @param root The JSON object containing config data
     * @return The config version number if found and valid, -1 if:
     *         <ul>
     *           <li>Required JSON structure doesn't exist</li>
     *           <li>Version field is missing</li>
     *           <li>Version value isn't an integer</li>
     *         </ul>
     */
    public static int tryGetConfigVersion(JsonObject root) {
        // MagicLib 0.8 config schema.
        JsonObject internal = JsonUtils.getNestedObject(root, "internal", false);

        if (internal != null) {
            JsonObject global = JsonUtils.getNestedObject(internal, "global", false);

            if (global == null) {
                return -1;
            }

            return JsonUtils.getIntegerOrDefault(global, "config_version", -1);
        }

        // MagicLib 0.3 config schema.
        return JsonUtils.getIntegerOrDefault(root, "configVersion", -1);
    }
}
