package top.hendrixshen.magiclib.config;

import com.google.common.collect.Lists;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.util.ActiveMode;
import fi.dy.masa.malilib.util.Color4f;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import top.hendrixshen.magiclib.MagicLib;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependencies;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.Dependency;
import top.hendrixshen.magiclib.api.dependencyValidator.annotation.OptionDependencyPredicate;
import top.hendrixshen.magiclib.api.malilib.annotation.Config;
import top.hendrixshen.magiclib.api.malilib.annotation.Hotkey;
import top.hendrixshen.magiclib.util.malilib.Option;

import java.util.ArrayList;

public class Configs {

    @Hotkey(hotkey = "M,A,G")
    @Config(category = ConfigCategory.GENERIC)
    public static ConfigHotkey openConfigGui;

    @Config(category = ConfigCategory.GENERIC)
    public static boolean debug = false;

    @Config(category = ConfigCategory.TEST, predicate = DebugPredicate.class)
    public static int intConfig = 0;

    @Config(category = ConfigCategory.TEST, predicate = DebugPredicate.class)
    public static String stringConfig = "string";

    @Config(category = ConfigCategory.TEST, predicate = DebugPredicate.class)
    public static boolean booleanConfig = false;

    @Hotkey
    @Config(category = ConfigCategory.TEST, predicate = DebugPredicate.class)
    public static boolean booleanHotkeyConfig = false;

    @Config(category = ConfigCategory.TEST, predicate = DebugPredicate.class)
    public static double doubleConfig = 0.1;

    @Config(category = ConfigCategory.TEST, predicate = DebugPredicate.class)
    public static Color4f colorConfig = Color4f.ZERO;

    @Config(category = ConfigCategory.TEST, predicate = DebugPredicate.class)
    public static ArrayList<String> stringListConfig = Lists.newArrayList("test1", "test2");

    @Config(category = ConfigCategory.TEST, predicate = DebugPredicate.class)
    public static IConfigOptionListEntry optionListConfig = ActiveMode.ALWAYS;

    @Config(category = ConfigCategory.TEST, predicate = DebugPredicate.class, dependencies = @Dependencies(require = @Dependency(value = "sodium", versionPredicates = "*")))
    public static boolean sodiumTest = false;

    public static class ConfigCategory {
        public static final String GENERIC = "generic";
        public static final String TEST = "test";
    }

    public static class DebugPredicate extends OptionDependencyPredicate {
        @Override
        public boolean test(Option option) {
            return debug;
        }
    }

    public static void init() {
        openConfigGui.getKeybind().setCallback((keyAction, iKeybind) -> {
            Minecraft.getInstance().setScreen(GuiConfigs.getInstance());
            return true;
        });

        MagicLib.cm.setValueChangeCallback("debug", option -> {
            if (debug) {
                Configurator.setLevel(MagicLibReference.getModId(), Level.toLevel("DEBUG"));
            } else {
                Configurator.setLevel(MagicLibReference.getModId(), Level.toLevel("INFO"));
            }
            GuiConfigs.getInstance().reDraw();
        });
        if (debug) {
            Configurator.setLevel(MagicLibReference.getModId(), Level.toLevel("DEBUG"));
        }
    }
}
