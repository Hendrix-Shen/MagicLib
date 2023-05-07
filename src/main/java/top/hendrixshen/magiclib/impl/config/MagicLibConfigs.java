package top.hendrixshen.magiclib.impl.config;

import com.google.common.collect.Lists;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.util.ActiveMode;
import fi.dy.masa.malilib.util.Color4f;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.api.annotation.Dependency;
import top.hendrixshen.magiclib.dependency.impl.ConfigDependencyPredicates;
import top.hendrixshen.magiclib.language.impl.MagicLanguageManager;
import top.hendrixshen.magiclib.malilib.api.annotation.Config;
import top.hendrixshen.magiclib.malilib.api.annotation.Hotkey;
import top.hendrixshen.magiclib.malilib.api.annotation.Numeric;
import top.hendrixshen.magiclib.malilib.impl.ConfigHandler;
import top.hendrixshen.magiclib.malilib.impl.ConfigManager;

import java.util.ArrayList;

@ApiStatus.Internal
@Environment(EnvType.CLIENT)
public class MagicLibConfigs {
    @Config(category = ConfigCategory.GENERIC)
    public static boolean debug = false;

    @Config(category = ConfigCategory.GENERIC)
    public static ArrayList<String> fallbackLanguageList = Lists.newArrayList(MagicLanguageManager.DEFAULT_CODE);

    @Hotkey(hotkey = "M,A,G")
    @Config(category = ConfigCategory.GENERIC)
    public static ConfigHotkey openConfigGui;

    @Config(category = ConfigCategory.TEST, predicate = ConfigDependencyPredicates.DebugConfigPredicate.class)
    public static boolean booleanConfig = false;

    @Hotkey
    @Config(category = ConfigCategory.TEST, predicate = ConfigDependencyPredicates.DebugConfigPredicate.class)
    public static boolean booleanHotkeyConfig = false;

    @Config(category = ConfigCategory.TEST, predicate = ConfigDependencyPredicates.DebugConfigPredicate.class)
    public static Color4f colorConfig = Color4f.ZERO;

    @Numeric(maxValue = 0.9, minValue = 0.1)
    @Config(category = ConfigCategory.TEST, predicate = ConfigDependencyPredicates.DebugConfigPredicate.class)
    public static double doubleConfig = 0.1;

    @Numeric(maxValue = 500, minValue = 0, useSlider = true)
    @Config(category = ConfigCategory.TEST, predicate = ConfigDependencyPredicates.DebugConfigPredicate.class)
    public static int intConfig = 0;

    @Config(category = ConfigCategory.TEST, predicate = ConfigDependencyPredicates.DebugConfigPredicate.class)
    public static IConfigOptionListEntry optionListConfig = ActiveMode.ALWAYS;

    @Config(category = ConfigCategory.TEST, predicate = ConfigDependencyPredicates.DebugConfigPredicate.class,
            dependencies = @Dependencies(and = @Dependency(value = "sodium", versionPredicate = ">=0.1")))
    public static boolean sodiumTest = false;

    @Config(category = ConfigCategory.TEST, predicate = ConfigDependencyPredicates.DebugConfigPredicate.class)
    public static String stringConfig = "string";

    @Config(category = ConfigCategory.TEST, predicate = ConfigDependencyPredicates.DebugConfigPredicate.class)
    public static ArrayList<String> stringListConfig = Lists.newArrayList("test1", "test2");

    private static final ArrayList<String> fallbackLanguageListOld = new ArrayList<>();

    private static boolean first = true;

    public static void init(@NotNull ConfigManager cm) {
        openConfigGui.getKeybind().setCallback((keyAction, iKeybind) -> {
            MagicLibConfigGui screen = MagicLibConfigGui.getInstance();
            //#if MC > 11903 && MC < 12000
            screen.setParent(Minecraft.getInstance().screen);
            //#else
            //$$ screen.setParentGui(Minecraft.getInstance().screen);
            //#endif
            Minecraft.getInstance().setScreen(screen);
            return true;
        });

        cm.setValueChangeCallback("debug", option -> {
            Configurator.setLevel(MagicLibReference.getModIdentifier(), MagicLibConfigs.debug ? Level.DEBUG : Level.INFO);
            MagicLibConfigGui.getInstance().reDraw();
        });
    }

    public static void postDeserialize(ConfigHandler configHandler) {
        Minecraft mc = Minecraft.getInstance();

        if (debug) {
            Configurator.setLevel(MagicLibReference.getModIdentifier(), Level.DEBUG);
        }

        if (first) {
            fallbackLanguageListOld.addAll(fallbackLanguageList);
            first = false;
            MagicLanguageManager.INSTANCE.initClient();
        }

        if (!fallbackLanguageListOld.equals(fallbackLanguageList)) {
            fallbackLanguageListOld.clear();
            fallbackLanguageListOld.addAll(fallbackLanguageList);
            MagicLanguageManager.INSTANCE.initClient();
        }
    }

    public static class ConfigCategory {
        public static final String GENERIC = "generic";
        public static final String TEST = "test";
    }
}
