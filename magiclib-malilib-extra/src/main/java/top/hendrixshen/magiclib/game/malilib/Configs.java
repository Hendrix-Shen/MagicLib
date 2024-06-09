package top.hendrixshen.magiclib.game.malilib;

import com.google.common.collect.ImmutableList;
import fi.dy.masa.malilib.config.options.ConfigBoolean;
import fi.dy.masa.malilib.interfaces.IValueChangeCallback;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.api.dependency.DependencyType;
import top.hendrixshen.magiclib.api.dependency.annotation.CompositeDependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.api.malilib.annotation.Config;
import top.hendrixshen.magiclib.api.malilib.annotation.Statistic;
import top.hendrixshen.magiclib.api.malilib.config.MagicConfigManager;
import top.hendrixshen.magiclib.api.malilib.config.option.EnumOptionEntry;
import top.hendrixshen.magiclib.impl.malilib.SharedConstants;
import top.hendrixshen.magiclib.impl.malilib.config.MagicConfigFactory;
import top.hendrixshen.magiclib.impl.malilib.config.option.*;
import top.hendrixshen.magiclib.impl.malilib.debug.MagicLibDebugHelper;

public class Configs {
    private static final MagicConfigManager cm = SharedConstants.getConfigManager();
    private static final MagicConfigFactory cf = Configs.cm.getConfigFactory();

    @Statistic(hotkey = false)
    @Config(category = ConfigCategory.GENERIC)
    public static MagicConfigHotkey openConfigGui = Configs.cf.newConfigHotkey("openConfigGui", "M,A,G");

    @Config(category = ConfigCategory.DEBUG)
    public static MagicConfigBoolean debug = Configs.cf.newConfigBoolean("debug", false);

    @Config(category = ConfigCategory.DEBUG, debugOnly = true)
    public static MagicConfigBoolean hideUnavailableConfigs = Configs.cf.newConfigBoolean("hideUnavailableConfigs", true);

    @Statistic(hotkey = false)
    @Config(category = ConfigCategory.DEBUG, debugOnly = true)
    public static MagicConfigHotkey resetAllConfigStatistic = Configs.cf.newConfigHotkey("resetAllConfigStatistic");

    @Statistic(hotkey = false)
    @Config(category = ConfigCategory.DEBUG, debugOnly = true)
    public static MagicConfigHotkey resetMagicLibConfigStatistic = Configs.cf.newConfigHotkey("resetMagicLibConfigStatistic");

    @Config(category = ConfigCategory.TEST, debugOnly = true)
    public static final MagicConfigBoolean testConfigBoolean = Configs.cf.newConfigBoolean("testConfigBoolean", false);

    @Config(category = ConfigCategory.TEST, debugOnly = true)
    public static final MagicConfigBooleanHotkeyed testConfigBooleanHotkeyed = Configs.cf.newConfigBooleanHotkeyed("testConfigBooleanHotkeyed", false);

    @Config(category = ConfigCategory.TEST, debugOnly = true)
    public static final MagicConfigColor testConfigColor = Configs.cf.newConfigColor("testConfigColor", "red");

    @Config(category = ConfigCategory.TEST, debugOnly = true)
    public static final MagicConfigDouble testConfigDouble = Configs.cf.newConfigDouble("testConfigDouble", 1.0, -5, 5);

    @Config(category = ConfigCategory.TEST, debugOnly = true)
    public static final MagicConfigHotkey testConfigHotkey = Configs.cf.newConfigHotkey("testConfigHotkey");

    @Config(category = ConfigCategory.TEST, debugOnly = true)
    public static final MagicConfigHotkeyWithSwitch testConfigHotkeyWithSwitch = Configs.cf.newConfigHotkeyWithSwitch("testConfigHotkeyWithSwitch", true);

    @Config(category = ConfigCategory.TEST, debugOnly = true)
    public static final MagicConfigInteger testConfigInteger = Configs.cf.newConfigInteger("testConfigInteger", 1, -5, 5);

    @Config(category = ConfigCategory.TEST, debugOnly = true)
    public static final MagicConfigOptionList testConfigOptionList = Configs.cf.newConfigOptionList("testConfigOptionList", Test.OptionListTest.DEFAULT);

    @Config(category = ConfigCategory.TEST, debugOnly = true)
    public static final MagicConfigOptionListHotkeyed testConfigOptionListHotkeyed = Configs.cf.newConfigOptionListHotkeyed("testConfigOptionListHotkeyed", Test.OptionListTest.DEFAULT);

    @Config(category = ConfigCategory.TEST, debugOnly = true)
    public static final MagicConfigString testConfigString = Configs.cf.newConfigString("testConfigString", "test");

    @Config(category = ConfigCategory.TEST, debugOnly = true)
    public static final MagicConfigStringList testConfigStringList = Configs.cf.newConfigStringList("testConfigStringList", ImmutableList.of("test1", "test2"));

    @Dependencies(
            conflict = @Dependency(dependencyType = DependencyType.MOD_ID, value = "minecraft", versionPredicates = "<2.0"),
            require = @Dependency(dependencyType = DependencyType.MOD_ID, value = "dummy-lib", versionPredicates = "*")
    )
    @Config(category = ConfigCategory.TEST, debugOnly = true)
    public static MagicConfigBoolean testDependencies = Configs.cf.newConfigBoolean("testDependencies", false);

    @Dependencies(
            conflict = @Dependency(dependencyType = DependencyType.MOD_ID, value = "minecraft", versionPredicates = "<2.0"),
            require = @Dependency(dependencyType = DependencyType.MOD_ID, value = "dummy-lib", versionPredicates = "*")
    )
    @Dependencies(
            conflict = @Dependency(dependencyType = DependencyType.MOD_ID, value = "magiclib_core", versionPredicates = "*"),
            require = {
                    @Dependency(dependencyType = DependencyType.MOD_ID, value = "minecraft", versionPredicates = "*"),
                    @Dependency(dependencyType = DependencyType.MOD_ID, value = "dummy", versionPredicates = ">0.15.5")
            }
    )
    @Config(category = ConfigCategory.TEST, debugOnly = true)
    public static MagicConfigBoolean testDependenciesComposite = Configs.cf.newConfigBoolean("testDependenciesComposite", false);

    public static void init() {
        Configs.cm.parseConfigClass(Configs.class);
        IValueChangeCallback<ConfigBoolean> redrawConfigGui = newValue -> ConfigGui.getCurrentInstance()
                .ifPresent(ConfigGui::reDraw);

        // Generic
        MagicConfigManager.setHotkeyCallback(Configs.openConfigGui, ConfigGui::openGui, true);

        // DEBUG
        Configs.debug.setValueChangeCallback(redrawConfigGui);
        Configs.hideUnavailableConfigs.setValueChangeCallback(redrawConfigGui);
        MagicConfigManager.setHotkeyCallback(Configs.resetAllConfigStatistic, MagicLibDebugHelper::resetAllConfigStatistic, false);
        MagicConfigManager.setHotkeyCallback(Configs.resetMagicLibConfigStatistic, MagicLibDebugHelper::resetMagicLibConfigStatistic, false);
    }

    private static class Test {
        public enum OptionListTest implements EnumOptionEntry {
            TEST_A, TEST_B, TEST_C;

            public static final OptionListTest DEFAULT = OptionListTest.TEST_B;

            @Override
            public EnumOptionEntry[] getAllValues() {
                return OptionListTest.values();
            }

            @Override
            public EnumOptionEntry getDefault() {
                return OptionListTest.DEFAULT;
            }

            @Override
            public @NotNull String getTranslationPrefix() {
                return String.format("%s.config.option.enumTest",
                        top.hendrixshen.magiclib.SharedConstants.getMagiclibIdentifier());
            }
        }
    }
}
