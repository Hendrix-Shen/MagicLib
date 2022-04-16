package top.hendrixshen.magiclib;

import com.google.common.collect.Lists;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.util.ActiveMode;
import fi.dy.masa.malilib.util.Color4f;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import top.hendrixshen.magiclib.compat.test.client.gui.screens.TestScreen;
import top.hendrixshen.magiclib.config.ConfigManager;
import top.hendrixshen.magiclib.config.annotation.Config;
import top.hendrixshen.magiclib.config.annotation.Hotkey;
import top.hendrixshen.magiclib.config.annotation.Numeric;
import top.hendrixshen.magiclib.dependency.Predicates;
import top.hendrixshen.magiclib.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.annotation.Dependency;

import java.util.ArrayList;

public class MagicLibConfigs {
    @Hotkey(hotkey = "M,A,G")
    @Config(category = ConfigCategory.GENERIC)
    public static ConfigHotkey openConfigGui;

    @Config(category = ConfigCategory.GENERIC)
    public static boolean debug = false;

    @Numeric(maxValue = 500, minValue = 0, useSlider = true)
    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static int intConfig = 0;

    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static String stringConfig = "string";

    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static boolean booleanConfig = false;

    @Hotkey
    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static boolean booleanHotkeyConfig = false;

    @Numeric(maxValue = 0.9, minValue = 0.1)
    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static double doubleConfig = 0.1;

    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static Color4f colorConfig = Color4f.ZERO;

    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static ArrayList<String> stringListConfig = Lists.newArrayList("test1", "test2");

    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static IConfigOptionListEntry optionListConfig = ActiveMode.ALWAYS;

    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class,
            dependencies = @Dependencies(and = @Dependency(value = "sodium", versionPredicate = ">=0.1")))
    public static boolean sodiumTest = false;

    @Hotkey(hotkey = "M,A,B")
    @Config(category = ConfigCategory.TEST)
    public static ConfigHotkey openButtonTestScreen;

    public static void init(ConfigManager cm) {
        openConfigGui.getKeybind().setCallback((keyAction, iKeybind) -> {
            Minecraft.getInstance().setScreen(MagicLibConfigGui.getInstance());
            return true;
        });

        openButtonTestScreen.getKeybind().setCallback((keyAction, iKeybind) -> {
            Minecraft.getInstance().setScreen(new TestScreen());
            return true;
        });

        cm.setValueChangeCallback("debug", option -> {
            if (debug) {
                Configurator.setLevel(MagicLibReference.getModId(), Level.toLevel("DEBUG"));
            } else {
                Configurator.setLevel(MagicLibReference.getModId(), Level.toLevel("INFO"));
            }
            MagicLibConfigGui.getInstance().reDraw();
        });
        if (debug) {
            Configurator.setLevel(MagicLibReference.getModId(), Level.toLevel("DEBUG"));
        }
    }

    public static class ConfigCategory {
        public static final String GENERIC = "generic";
        public static final String TEST = "test";
    }
}
