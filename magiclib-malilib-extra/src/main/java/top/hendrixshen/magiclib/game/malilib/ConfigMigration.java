package top.hendrixshen.magiclib.game.malilib;

import com.google.common.collect.ImmutableList;

import top.hendrixshen.magiclib.impl.malilib.SharedConstants;
import top.hendrixshen.magiclib.impl.malilib.config.MagicConfigHandlerImpl;
import top.hendrixshen.magiclib.impl.malilib.config.migration.ConfigRelocationMigrator;
import top.hendrixshen.magiclib.impl.malilib.config.migration.ConfigRelocationMigrator.MigrationMapping;
import top.hendrixshen.magiclib.impl.malilib.config.migration.VersionMigrator;

import java.util.function.Supplier;

public class ConfigMigration {
    private static final ImmutableList<MigrationMapping> V1_TO_V2 = ImmutableList.of(
            new MigrationMapping("generic", "debug", "debug", "debug"),
            new MigrationMapping("test", "stringConfig", "test", "testConfigString"),
            new MigrationMapping("test", "colorConfig", "test", "testConfigColor"),
            new MigrationMapping("test", "booleanConfig", "test", "testConfigBoolean"),
            new MigrationMapping("test", "optionListConfig", "test", "testConfigOptionList"),
            new MigrationMapping("test", "doubleConfig", "test", "testConfigDouble"),
            new MigrationMapping("test", "intConfig", "test", "testConfigInteger"),
            new MigrationMapping("test", "stringListConfig", "test", "testConfigStringList"),
            new MigrationMapping("test", "booleanHotkeyConfig", "test", "testConfigBooleanHotkeyed")
    );

    public static void setup() {
        MagicConfigHandlerImpl configHandler = (MagicConfigHandlerImpl) SharedConstants.getConfigHandler();
        Supplier<Integer> oldVersionSuppler = () -> VersionMigrator.tryGetConfigVersion(configHandler.getLoadedJson());

        // This wasn't supposed to be here,
        // due to an oversight since MagicLib 0.8 was released,
        // the config file version was incorrectly set to 0
        // Affected versions: 0.8.532 ~ 0.8.693
        configHandler.registerMigrator(new VersionMigrator(2, handler -> oldVersionSuppler.get() == 0));

        // Migrate from v1 to v2
        // For MagicLib 0.3 ~ 0.7
        configHandler.registerMigrator(new ConfigRelocationMigrator(ConfigMigration.V1_TO_V2, handler -> oldVersionSuppler.get() == 1));
        configHandler.registerMigrator(new VersionMigrator(2, handler -> oldVersionSuppler.get() == 1));
    }
}
