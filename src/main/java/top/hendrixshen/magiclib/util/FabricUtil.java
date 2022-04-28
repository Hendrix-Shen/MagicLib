package top.hendrixshen.magiclib.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;
import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.metadata.version.VersionPredicate;
import top.hendrixshen.magiclib.MagicLibReference;
import top.hendrixshen.magiclib.dependency.DepCheckException;
import top.hendrixshen.magiclib.dependency.Dependencies;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.*;
import java.util.function.BiConsumer;

public class FabricUtil {
    // Fabric Loader 0.11 and below support
    private static Method fabricLegacyVersionPredicateParser;
    private static Method fabricLegacyDisplayCriticalError;

    private static Method fabricVersionPredicateParser;
    private static Method fabricDisplayCriticalError;

    private static Method quiltVersionPredicateParser;

    private static Method quiltDisplayCriticalError;


    static {
        try {
            fabricLegacyVersionPredicateParser = Class.forName("net.fabricmc.loader.util.version.VersionPredicateParser").getMethod("matches", Version.class, String.class);
            fabricLegacyDisplayCriticalError = Class.forName("net.fabricmc.loader.gui.FabricGuiEntry").getMethod("displayCriticalError", Throwable.class, boolean.class);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }

        try {
            fabricVersionPredicateParser = Class.forName("net.fabricmc.loader.impl.util.version.VersionPredicateParser").getMethod("parse", String.class);
            fabricDisplayCriticalError = Class.forName("net.fabricmc.loader.impl.gui.FabricGuiEntry").getMethod("displayCriticalError", Throwable.class, boolean.class);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }

        try {
            quiltVersionPredicateParser = Class.forName("org.quiltmc.loader.impl.util.version.VersionPredicateParser").getMethod("parse", String.class);
            quiltDisplayCriticalError = Class.forName("org.quiltmc.loader.impl.gui.QuiltGuiEntry").getMethod("displayError",
                    String.class, Throwable.class, boolean.class, boolean.class);
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
        }
    }

    /**
     * Verify that the Fabric Loader has loaded the qualified mods.
     *
     * @param version          Version provided by the fabric loader.
     * @param versionPredicate Semantic versioning expression.
     * @return True if the Fabric Loader finds a matching mods from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(Version version, String versionPredicate) {
        try {
            if (fabricLegacyVersionPredicateParser != null) {
                try {
                    return (boolean) fabricLegacyVersionPredicateParser.invoke(null, version, versionPredicate);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    MagicLibReference.LOGGER.error("Failed to invoke fabricLegacyVersionPredicateParser#matches", e);
                    throw new RuntimeException(e);
                }
            } else if (fabricVersionPredicateParser != null) {
                try {
                    return ((VersionPredicate) fabricVersionPredicateParser.invoke(null, versionPredicate)).test(version);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    MagicLibReference.LOGGER.error("Failed to invoke fabricVersionPredicateParser#parse", e);
                    throw new RuntimeException(e);
                }
            } else {
                try {
                    return ((VersionPredicate) quiltVersionPredicateParser.invoke(null, versionPredicate)).test(version);
                } catch (InvocationTargetException | IllegalAccessException e) {
                    MagicLibReference.LOGGER.error("Failed to invoke quiltVersionPredicateParser#parse", e);
                    throw new RuntimeException(e);
                }
            }

        } catch (Throwable e) {
            MagicLibReference.LOGGER.error("Failed to parse version or version predicate {} {}: {}", version.getFriendlyString(), versionPredicate, e);
            return false;
        }
    }

    /**
     * Verify that the Fabric Loader has loaded the qualified mods.
     *
     * @param modId Version provided by the fabric loader.
     * @return True if the Fabric Loader finds a matching mod from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(String modId) {
        return FabricLoader.getInstance().isModLoaded(modId);
    }

    /**
     * Verify that the Fabric Loader has loaded the qualified mod.
     *
     * @param modId            Version provided by the fabric loader.
     * @param versionPredicate Semantic versioning expression.
     * @return True if the Fabric Loader finds a matching mod from the list of
     * loaded mods, false otherwise.
     */
    public static boolean isModLoaded(String modId, String versionPredicate) {
        Optional<ModContainer> modContainerOptional = FabricLoader.getInstance().getModContainer(modId);
        if (modContainerOptional.isPresent()) {
            ModContainer modContainer = modContainerOptional.get();
            return isModLoaded(modContainer.getMetadata().getVersion(), versionPredicate);
        }
        return false;
    }

    public static class ModMetaData {
        public static HashMap<String, ModMetaData> data = new HashMap<>();

        public String id;

        public JsonObject json;

        public HashMap<String, HashSet<String>> entrypoints;

        private ModMetaData(JsonObject json) {
            this.id = json.get("id").getAsString();
            this.json = json;
            this.entrypoints = new HashMap<>();

            JsonObject entrypointsJsonObject = json.getAsJsonObject("entrypoints");
            if (entrypointsJsonObject != null) {
                for (Map.Entry<String, JsonElement> entrypointEntry : entrypointsJsonObject.entrySet()) {
                    JsonArray entrypointArray = entrypointEntry.getValue().getAsJsonArray();
                    for (int i = 0; i < entrypointArray.size(); ++i) {
                        String entrypoint = entrypointArray.get(i).getAsString();
                        HashSet<String> entrypointSet = entrypoints.computeIfAbsent(
                                entrypointEntry.getKey(), key -> new HashSet<>());
                        entrypointSet.add(entrypoint);
                    }
                }
            }

        }

        static {
            URL logUrl = null;
            try {
                for (URL url : getResources("fabric.mod.json")) {
                    logUrl = url;
                    JsonObject jsonObject = MiscUtil.readJson(url);
                    try {
                        ModMetaData modMetaData = new ModMetaData(jsonObject);
                        data.put(modMetaData.id, modMetaData);
                    } catch (Throwable e) {
                        MagicLibReference.LOGGER.debug("Exception when parse {}.", url);
                    }
                }
            } catch (IOException e) {
                MagicLibReference.LOGGER.error("Exception when parse {}.", logUrl);
                FabricUtil.displayCriticalError(e);
            }
        }
    }

    public static Set<URL> getResources(String name) throws IOException {
        ClassLoader urlLoader = Thread.currentThread().getContextClassLoader();
        HashSet<URL> hashSet = new HashSet<>();
        Enumeration<URL> urlEnumeration = urlLoader.getResources(name);
        while (urlEnumeration.hasMoreElements()) {
            hashSet.add(urlEnumeration.nextElement());
        }
        return hashSet;
    }


    private static Map<String, Dependencies<Object>> getModInitDependencies(String entryKey, String entryMethod) {

        Map<String, Dependencies<Object>> ret = new HashMap<>();
        for (ModMetaData modMetaData : ModMetaData.data.values()) {

            for (String entrypointValue : modMetaData.entrypoints.getOrDefault(entryKey, new HashSet<>())) {
                Dependencies<Object> dependencies = Dependencies.getFabricEntrypointDependencies(entrypointValue, entryMethod);
                if (dependencies != null) {
                    ret.put(modMetaData.id, dependencies);
                }
            }
        }
        return ret;
    }

    /**
     * Interrupted only if the FabricLoader loads a mod version that does not
     * match the rules in the list.
     */
    public static void compatVersionCheck() {
        FabricLoader fabricLoader = FabricLoader.getInstance();

        StringBuilder result = new StringBuilder();
        BiConsumer<String, Dependencies<Object>> depCheckCallback = (modId, dependencies) -> {
            String depResult = dependencies.getCheckResult(null);
            if (!depResult.equals(Dependencies.SATISFIED)) {
                if (result.length() != 0) {
                    result.append("\n");
                }
                result.append(String.format("Mod %s compat version check failed.\n", modId));
                result.append(depResult);
            }
        };

        getModInitDependencies("main", "onInitialize").forEach(depCheckCallback);
        if (fabricLoader.getEnvironmentType() == EnvType.CLIENT) {
            getModInitDependencies("client", "onInitializeClient").forEach(depCheckCallback);
        } else {
            getModInitDependencies("server", "onInitializeServer").forEach(depCheckCallback);
        }

        if (result.length() != 0) {
            displayCriticalError(new DepCheckException(String.format("Mod resolution encountered an incompatible mod set!\n %s", result)));
        }
    }

    /**
     * Exposing the Fabric loader graphical exception display window method.
     *
     * @param exception Thrown exceptions.
     */
    public static void displayCriticalError(Throwable exception) {
        exception.printStackTrace();
        String nm = System.getProperty("java.awt.headless");
        if (Boolean.parseBoolean(nm)) {
            System.setProperty("java.awt.headless", "");
        }
        if (fabricLegacyDisplayCriticalError != null) {
            try {
                fabricLegacyDisplayCriticalError.invoke(null, exception, true);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else if (fabricDisplayCriticalError != null) {
            try {
                fabricDisplayCriticalError.invoke(null, exception, true);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        } else {
            try {
                quiltDisplayCriticalError.invoke(null, "Error", exception, true, true);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * @return True if started with Fabric Loom.
     */
    public static boolean isDevelopmentEnvironment() {
        return FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
