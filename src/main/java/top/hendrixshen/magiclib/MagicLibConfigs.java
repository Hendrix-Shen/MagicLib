package top.hendrixshen.magiclib;

import com.google.common.collect.Lists;
import fi.dy.masa.malilib.config.IConfigOptionListEntry;
import fi.dy.masa.malilib.config.options.ConfigHotkey;
import fi.dy.masa.malilib.util.ActiveMode;
import fi.dy.masa.malilib.util.Color4f;
import net.minecraft.client.Minecraft;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.config.Configurator;
import org.jetbrains.annotations.NotNull;
import top.hendrixshen.magiclib.config.ConfigHandler;
import top.hendrixshen.magiclib.config.ConfigManager;
import top.hendrixshen.magiclib.config.annotation.Config;
import top.hendrixshen.magiclib.config.annotation.Hotkey;
import top.hendrixshen.magiclib.config.annotation.Numeric;
import top.hendrixshen.magiclib.dependency.Predicates;
import top.hendrixshen.magiclib.dependency.annotation.Dependencies;
import top.hendrixshen.magiclib.dependency.annotation.Dependency;
import top.hendrixshen.magiclib.language.MagicLanguageManager;
import top.hendrixshen.magiclib.tool.doc.CarpetDocumentGenerator;
import top.hendrixshen.magiclib.tool.doc.ConfigDocumentGenerator;

import java.util.ArrayList;

public class MagicLibConfigs {
    @Config(category = ConfigCategory.GENERIC)
    public static boolean debug = false;

    @Config(category = ConfigCategory.GENERIC)
    public static ArrayList<String> fallbackLanguageList = Lists.newArrayList(MagicLanguageManager.DEFAULT_CODE);

    @Hotkey(hotkey = "M,A,G")
    @Config(category = ConfigCategory.GENERIC)
    public static ConfigHotkey openConfigGui;

    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static boolean booleanConfig = false;

    @Hotkey
    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static boolean booleanHotkeyConfig = false;

    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static Color4f colorConfig = Color4f.ZERO;

    @Numeric(maxValue = 0.9, minValue = 0.1)
    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static double doubleConfig = 0.1;

    @Numeric(maxValue = 500, minValue = 0, useSlider = true)
    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static int intConfig = 0;

    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static IConfigOptionListEntry optionListConfig = ActiveMode.ALWAYS;

    @Hotkey()
    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static ConfigHotkey printDoc;

    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class,
            dependencies = @Dependencies(and = @Dependency(value = "sodium", versionPredicate = ">=0.1")))
    public static boolean sodiumTest = false;

    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static String stringConfig = "string";

    @Config(category = ConfigCategory.TEST, predicate = Predicates.DebugOptionPredicate.class)
    public static ArrayList<String> stringListConfig = Lists.newArrayList("test1", "test2");

    private static ArrayList<String> fallbackLanguageListOld = new ArrayList<>();

    private static boolean first = true;

    public static void init(@NotNull ConfigManager cm) {
        openConfigGui.getKeybind().setCallback((keyAction, iKeybind) -> {
            MagicLibConfigGui screen = MagicLibConfigGui.getInstance();
            screen.setParentGui(Minecraft.getInstance().screen);
            Minecraft.getInstance().setScreen(screen);
            return true;
        });

        cm.setValueChangeCallback("debug", option -> {
            Configurator.setLevel(MagicLibReference.getModId(), MagicLibConfigs.debug ? Level.DEBUG : Level.INFO);
            if (debug) {
                Configurator.setLevel(MagicLibReference.getModId(), Level.toLevel("DEBUG"));
            } else {
                Configurator.setLevel(MagicLibReference.getModId(), Level.toLevel("INFO"));
            }
            MagicLibConfigGui.getInstance().reDraw();
        });

        printDoc.getKeybind().setCallback(((keyAction, iKeybind) -> {
            ConfigDocumentGenerator configDocumentGenerator = new ConfigDocumentGenerator(MagicLibReference.getModId());
            configDocumentGenerator.genFile();
            configDocumentGenerator.setCurrentLanguageCode("zh_cn");
            configDocumentGenerator.genFile();
            CarpetDocumentGenerator carpetDocumentGenerator = new CarpetDocumentGenerator(MagicLibReference.getModId());
            carpetDocumentGenerator.setCurrentLanguageCode("en_us");
            carpetDocumentGenerator.genFile();
            carpetDocumentGenerator.setCurrentLanguageCode("zh_cn");
            carpetDocumentGenerator.genFile();
            return true;
        }));
    }

    public static void postDeserialize(ConfigHandler configHandler) {
        Minecraft mc = Minecraft.getInstance();
        if (debug) {
            Configurator.setLevel(MagicLibReference.getModId(), Level.DEBUG);
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
            mc.getLanguageManager().onResourceManagerReload(mc.getResourceManager());
        }
    }

    public static class ConfigCategory {
        public static final String GENERIC = "generic";
        public static final String TEST = "test";
    }
}
